package com.example.ui.screens.pronunciation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.PronunciationAnalysis
import com.example.data.model.PronunciationData
import com.example.data.model.PronunciationExercise
import com.example.data.remote.GeminiClient
import com.example.data.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PronunciationViewModel(
    private val geminiClient: GeminiClient,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val exercises: List<PronunciationExercise> = PronunciationData.exercises

    private val _selectedExerciseIndex = MutableStateFlow(0)
    val selectedExerciseIndex: StateFlow<Int> = _selectedExerciseIndex.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisResult = MutableStateFlow<PronunciationAnalysis?>(null)
    val analysisResult: StateFlow<PronunciationAnalysis?> = _analysisResult.asStateFlow()

    val currentExercise: PronunciationExercise
        get() = exercises.getOrElse(_selectedExerciseIndex.value) { exercises.first() }

    fun selectExercise(index: Int) {
        _selectedExerciseIndex.value = index
        _analysisResult.value = null
    }

    fun nextExercise() {
        if (_selectedExerciseIndex.value < exercises.size - 1) {
            _selectedExerciseIndex.value += 1
            _analysisResult.value = null
        }
    }

    fun evaluateSpokenSpeech(spokenText: String) {
        if (spokenText.isBlank()) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val result = geminiClient.evaluatePronunciation(
                    targetText = currentExercise.targetPhrase,
                    spokenText = spokenText
                )
                _analysisResult.value = result
                progressRepository.addSpeakingTime(15)
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun resetEvaluation() {
        _analysisResult.value = null
    }
}

class PronunciationViewModelFactory(
    private val geminiClient: GeminiClient,
    private val progressRepository: ProgressRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PronunciationViewModel(geminiClient, progressRepository) as T
    }
}
