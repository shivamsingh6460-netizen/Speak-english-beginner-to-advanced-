package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import kotlin.math.log10

/**
 * AudioRecorder helper class wrapping Android MediaRecorder API to capture user speech
 * for the pronunciation coach, fluency diagnostics, and offline voice playback comparisons.
 */
class AudioRecorder(private val context: Context) {

    companion object {
        private const val TAG = "AudioRecorder"
        private const val DEFAULT_AUDIO_EXT = ".m4a"
    }

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentOutputFile: File? = null
    private var amplitudeJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _lastRecordedFile = MutableStateFlow<File?>(null)
    val lastRecordedFile: StateFlow<File?> = _lastRecordedFile.asStateFlow()

    private val _maxAmplitude = MutableStateFlow(0)
    val maxAmplitude: StateFlow<Int> = _maxAmplitude.asStateFlow()

    private val _rmsDb = MutableStateFlow(0f)
    val rmsDb: StateFlow<Float> = _rmsDb.asStateFlow()

    /**
     * Starts recording audio from the microphone and stores it in the app's cache directory
     * or at a custom destination file.
     *
     * @param outputFile Optional custom destination file. If null, a timestamped m4a file in cacheDir is created.
     * @return The File instance where recording will be saved, or null if initialization failed.
     */
    fun startRecording(outputFile: File? = null): File? {
        try {
            stopPlayback()
            stopRecording()

            val targetFile = outputFile ?: createDefaultOutputFile()
            currentOutputFile = targetFile

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(targetFile.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            _isRecording.value = true
            _isPaused.value = false

            startAmplitudePolling()
            Log.d(TAG, "Recording started -> ${targetFile.absolutePath}")
            return targetFile
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize MediaRecorder: ${e.message}", e)
            releaseRecorder()
            return null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "MediaRecorder in illegal state: ${e.message}", e)
            releaseRecorder()
            return null
        } catch (e: SecurityException) {
            Log.e(TAG, "RECORD_AUDIO permission not granted: ${e.message}", e)
            releaseRecorder()
            return null
        }
    }

    /**
     * Stops the active recording session and returns the recorded audio file.
     */
    fun stopRecording(): File? {
        if (!_isRecording.value && mediaRecorder == null) {
            return _lastRecordedFile.value
        }

        stopAmplitudePolling()

        try {
            mediaRecorder?.apply {
                try {
                    stop()
                } catch (e: RuntimeException) {
                    // Handle case where stop() is called immediately after start() before frames are captured
                    Log.w(TAG, "Audio recording was too short to produce valid frames: ${e.message}")
                    currentOutputFile?.delete()
                }
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while stopping MediaRecorder: ${e.message}", e)
        } finally {
            mediaRecorder = null
            _isRecording.value = false
            _isPaused.value = false
            _maxAmplitude.value = 0
            _rmsDb.value = 0f
        }

        val completedFile = currentOutputFile
        if (completedFile != null && completedFile.exists() && completedFile.length() > 0) {
            _lastRecordedFile.value = completedFile
            Log.d(TAG, "Recording successfully finalized: ${completedFile.length()} bytes")
        }

        return _lastRecordedFile.value
    }

    /**
     * Pauses the current recording (Android N / API 24+).
     */
    fun pauseRecording() {
        if (_isRecording.value && !_isPaused.value) {
            try {
                mediaRecorder?.pause()
                _isPaused.value = true
                stopAmplitudePolling()
                Log.d(TAG, "Recording paused")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to pause recording: ${e.message}", e)
            }
        }
    }

    /**
     * Resumes the paused recording (Android N / API 24+).
     */
    fun resumeRecording() {
        if (_isRecording.value && _isPaused.value) {
            try {
                mediaRecorder?.resume()
                _isPaused.value = false
                startAmplitudePolling()
                Log.d(TAG, "Recording resumed")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to resume recording: ${e.message}", e)
            }
        }
    }

    /**
     * Cancels the active recording and deletes any partial audio file.
     */
    fun cancelRecording() {
        stopAmplitudePolling()
        try {
            mediaRecorder?.apply {
                try {
                    stop()
                } catch (_: Exception) {}
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cancelling recording: ${e.message}", e)
        } finally {
            mediaRecorder = null
            _isRecording.value = false
            _isPaused.value = false
            _maxAmplitude.value = 0
            _rmsDb.value = 0f
            currentOutputFile?.let {
                if (it.exists()) it.delete()
            }
            currentOutputFile = null
        }
    }

    /**
     * Plays back a recorded audio file so users can evaluate their pronunciation.
     */
    fun playRecording(file: File? = null, onCompletion: () -> Unit = {}) {
        val targetFile = file ?: _lastRecordedFile.value
        if (targetFile == null || !targetFile.exists()) {
            Log.w(TAG, "No valid audio file available for playback")
            return
        }

        stopPlayback()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(targetFile.absolutePath)
                prepare()
                setOnCompletionListener {
                    _isPlaying.value = false
                    onCompletion()
                }
                start()
            }
            _isPlaying.value = true
            Log.d(TAG, "Playback started for: ${targetFile.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Playback failed: ${e.message}", e)
            _isPlaying.value = false
            stopPlayback()
        }
    }

    /**
     * Stops any ongoing playback session.
     */
    fun stopPlayback() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping media player: ${e.message}", e)
        } finally {
            mediaPlayer = null
            _isPlaying.value = false
        }
    }

    /**
     * Releases all hardware and software resources. Call when leaving screens or destroying activities.
     */
    fun release() {
        cancelRecording()
        stopPlayback()
    }

    private fun startAmplitudePolling() {
        stopAmplitudePolling()
        amplitudeJob = scope.launch {
            while (isActive && _isRecording.value && !_isPaused.value) {
                try {
                    val amp = mediaRecorder?.maxAmplitude ?: 0
                    _maxAmplitude.value = amp

                    // Convert raw amplitude (0..32767) into a decibel-like scale (-2..10 dB)
                    val db = if (amp > 0) {
                        (20 * log10(amp.toDouble() / 32767.0) + 40).coerceIn(-2.0, 12.0).toFloat()
                    } else {
                        -2f
                    }
                    _rmsDb.value = db
                } catch (e: Exception) {
                    Log.w(TAG, "Error polling amplitude: ${e.message}")
                }
                delay(75)
            }
        }
    }

    private fun stopAmplitudePolling() {
        amplitudeJob?.cancel()
        amplitudeJob = null
    }

    private fun releaseRecorder() {
        stopAmplitudePolling()
        try {
            mediaRecorder?.reset()
            mediaRecorder?.release()
        } catch (_: Exception) {}
        mediaRecorder = null
        _isRecording.value = false
        _isPaused.value = false
        _maxAmplitude.value = 0
        _rmsDb.value = 0f
    }

    private fun createDefaultOutputFile(): File {
        val storageDir = File(context.cacheDir, "pronunciation_coach_audio")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, "speech_${System.currentTimeMillis()}$DEFAULT_AUDIO_EXT")
    }
}
