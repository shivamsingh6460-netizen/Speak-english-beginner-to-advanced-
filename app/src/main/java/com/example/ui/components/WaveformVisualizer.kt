package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaveformVisualizer(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 18,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    maxHeight: Dp = 40.dp
) {
    val transition = rememberInfiniteTransition(label = "waveform")
    val animProgress by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_anim"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val staticHeights = listOf(
            0.3f, 0.6f, 0.8f, 0.4f, 0.9f, 0.5f, 0.7f, 1.0f, 0.8f,
            0.6f, 0.9f, 0.4f, 0.7f, 0.5f, 0.8f, 0.3f, 0.6f, 0.4f
        )

        for (i in 0 until barCount) {
            val baseRatio = staticHeights[i % staticHeights.size]
            val heightRatio = if (isActive) {
                (baseRatio * animProgress).coerceIn(0.15f, 1.0f)
            } else {
                0.2f
            }

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(maxHeight * heightRatio)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isActive) activeColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
            )
            Box(modifier = Modifier.width(3.dp))
        }
    }
}
