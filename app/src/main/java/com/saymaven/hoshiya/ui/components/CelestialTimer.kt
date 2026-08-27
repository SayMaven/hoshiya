package com.saymaven.hoshiya.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saymaven.hoshiya.core.model.TimerMode
import com.saymaven.hoshiya.core.model.TimerState
import com.saymaven.hoshiya.core.theme.TextPrimary
import com.saymaven.hoshiya.core.theme.TextTertiary

@Composable
fun CelestialTimer(
    remainingSeconds: Int,
    totalDurationSeconds: Int,
    mode: TimerMode,
    timerState: TimerState,
    onTimerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)
    val accentColor = mode.accentColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTimerClick
            )
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Clean Digits Display (No circles, No top badge)
            Text(
                text = timeFormatted,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 76.sp,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraLight
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // State helper label
            Text(
                text = when (timerState) {
                    TimerState.RUNNING -> "Tap to pause"
                    TimerState.PAUSED -> "Paused • Tap to resume"
                    TimerState.IDLE -> "Tap to begin"
                },
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp
                ),
                color = if (timerState == TimerState.PAUSED) accentColor else TextTertiary
            )
        }
    }
}
