package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

/**
 * Custom Compose component to visualize real-time audio input while practicing pronunciation.
 * Provides immediate visual feedback reacting dynamically to voice volume (dB), pitch modulation,
 * and recording status.
 *
 * @param isRecording True when microphone is actively capturing user speech
 * @param rmsDb Real-time speech decibel level supplied by SpeechRecognizer (typically -2 to 10 dB)
 * @param modifier Custom layout modifier
 * @param barCount Number of animated frequency equalizer bars
 * @param height Height of the visualization canvas
 * @param showGuidanceBadge Shows real-time speaking guidance (e.g. Optimal, Speak Louder, Ready)
 */
@Composable
fun RealtimeAudioVisualizer(
    isRecording: Boolean,
    rmsDb: Float = 0f,
    modifier: Modifier = Modifier,
    barCount: Int = 24,
    height: Dp = 64.dp,
    showGuidanceBadge: Boolean = true
) {
    // Normalize raw rmsDb (-2..10+) into a smooth 0.0..1.0 intensity scale
    val normalizedRms = if (isRecording) {
        ((rmsDb + 2f) / 10f).coerceIn(0.08f, 1.0f)
    } else {
        0.05f
    }

    // Smooth physics-based animation for audio amplitude
    val smoothIntensity by animateFloatAsState(
        targetValue = normalizedRms,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label = "audio_intensity_anim"
    )

    // Continuous wave phase for natural sinusoidal ripple motion
    val infiniteTransition = rememberInfiniteTransition(label = "audio_wave_infinite")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase_anim"
    )

    // Color pulses matching voice volume
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val errorColor = MaterialTheme.colorScheme.error
    val outlineColor = MaterialTheme.colorScheme.outline

    val activeGlowColor by animateColorAsState(
        targetValue = when {
            !isRecording -> outlineColor.copy(alpha = 0.3f)
            smoothIntensity > 0.65f -> errorColor // Energetic loud speech
            smoothIntensity > 0.3f -> secondaryColor // Clear speaking volume
            else -> primaryColor // Subtle vocalization
        },
        animationSpec = tween(200),
        label = "active_glow_color"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("realtime_audio_visualizer"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Equalizer Canvas with custom bezier-like frequency bars
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isRecording) activeGlowColor.copy(alpha = 0.08f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
                .border(
                    width = 1.dp,
                    color = if (isRecording) activeGlowColor.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val totalBars = barCount.coerceAtLeast(8)
                val spacing = 4.dp.toPx()
                val totalSpacing = spacing * (totalBars - 1)
                val barWidth = ((canvasWidth - totalSpacing) / totalBars).coerceAtLeast(3.dp.toPx())

                val midY = canvasHeight / 2f

                for (i in 0 until totalBars) {
                    val normalizedIndex = i.toFloat() / (totalBars - 1)
                    // Bell-curve distribution to make center bars taller
                    val bellCurve = sin(normalizedIndex * Math.PI).toFloat()

                    // Dynamic wave modulation based on audio input and sine phase
                    val waveModulation = sin(phase + i * 0.35f) * 0.25f + 0.75f

                    val barHeight = if (isRecording) {
                        val computed = canvasHeight * (smoothIntensity * 0.85f * bellCurve * waveModulation.toFloat())
                        computed.coerceIn(6.dp.toPx(), canvasHeight * 0.92f)
                    } else {
                        (canvasHeight * 0.15f * bellCurve).coerceAtLeast(4.dp.toPx())
                    }

                    val left = i * (barWidth + spacing)
                    val top = midY - (barHeight / 2f)

                    // Gradient shader for each bar
                    val barBrush = Brush.verticalGradient(
                        colors = if (isRecording) {
                            listOf(
                                primaryColor,
                                if (i % 2 == 0) secondaryColor else tertiaryColor,
                                activeGlowColor
                            )
                        } else {
                            listOf(
                                outlineColor.copy(alpha = 0.45f),
                                outlineColor.copy(alpha = 0.25f)
                            )
                        },
                        startY = top,
                        endY = top + barHeight
                    )

                    drawRoundRect(
                        brush = barBrush,
                        topLeft = Offset(left, top),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                    )
                }

                // Subtle center baseline wireframe
                if (isRecording) {
                    drawLine(
                        color = activeGlowColor.copy(alpha = 0.25f),
                        start = Offset(0f, midY),
                        end = Offset(canvasWidth, midY),
                        strokeWidth = 1.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Real-Time Visual Feedback & Voice Guidance Badge
        if (showGuidanceBadge) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isRecording) activeGlowColor.copy(alpha = 0.14f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = when {
                        !isRecording -> Icons.Default.Mic
                        smoothIntensity > 0.35f -> Icons.Default.VolumeUp
                        else -> Icons.Default.GraphicEq
                    },
                    contentDescription = null,
                    tint = if (isRecording) activeGlowColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when {
                        !isRecording -> "माइक पर टैप करके बोलें (Tap mic to speak)"
                        smoothIntensity > 0.65f -> "उत्कृष्ट ध्वनि स्तर! (Great voice volume!)"
                        smoothIntensity > 0.25f -> "स्पष्ट आवाज रिकॉर्ड हो रही है (Capturing clearly...)"
                        else -> "कृपया थोड़ा तेज़ और साफ बोलें (Speak a bit louder)"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    color = if (isRecording) activeGlowColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
