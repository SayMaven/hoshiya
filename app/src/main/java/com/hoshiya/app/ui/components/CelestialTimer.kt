package com.hoshiya.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoshiya.app.core.model.TimerMode
import com.hoshiya.app.core.model.TimerState
import com.hoshiya.app.core.theme.SurfaceBorder
import com.hoshiya.app.core.theme.TextPrimary
import com.hoshiya.app.core.theme.TextSecondary
import com.hoshiya.app.core.theme.TextTertiary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CelestialTimer(
    remainingSeconds: Int,
    totalDurationSeconds: Int,
    mode: TimerMode,
    timerState: TimerState,
    onTimerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (totalDurationSeconds > 0) {
        remainingSeconds.toFloat() / totalDurationSeconds.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "timer_progress"
    )

    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    val accentColor = mode.accentColor

    Box(
        modifier = modifier
            .size(280.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTimerClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Celestial Canvas Ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameter = size.minDimension
            val strokeWidth = 8.dp.toPx()
            val arcSize = Size(diameter - strokeWidth * 2, diameter - strokeWidth * 2)
            val topLeft = Offset(strokeWidth, strokeWidth)

            // Background Track Ring
            drawArc(
                color = SurfaceBorder.copy(alpha = 0.6f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 0.75f, cap = StrokeCap.Round)
            )

            // Active Glowing Arc
            val sweepAngle = animatedProgress * 360f
            if (sweepAngle > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.5f),
                            accentColor
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Glowing Star Orb at current angle
                val angleRad = ((-90f + sweepAngle) * PI / 180f).toFloat()
                val radius = (diameter - strokeWidth * 2) / 2f
                val center = Offset(diameter / 2f, diameter / 2f)
                val starX = center.x + radius * cos(angleRad)
                val starY = center.y + radius * sin(angleRad)

                // Outer soft halo
                drawCircle(
                    color = accentColor.copy(alpha = 0.35f),
                    radius = strokeWidth * 1.5f,
                    center = Offset(starX, starY)
                )

                // Inner bright orb
                drawCircle(
                    color = Color.White,
                    radius = strokeWidth * 0.65f,
                    center = Offset(starX, starY)
                )
            }
        }

        // Distraction-Free Center Details
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Japanese mode badge
            Surface(
                color = accentColor.copy(alpha = 0.12f),
                shape = CircleShape
            ) {
                Text(
                    text = "✦ ${mode.japaneseTag} ✦",
                    color = accentColor,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Time Display
            Text(
                text = timeFormatted,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 58.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                ),
                color = TextPrimary
            )

            // State helper label
            Text(
                text = when (timerState) {
                    TimerState.RUNNING -> "Tap to pause"
                    TimerState.PAUSED -> "Paused • Tap to resume"
                    TimerState.IDLE -> "Tap to begin"
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (timerState == TimerState.PAUSED) accentColor else TextTertiary
            )
        }
    }
}
