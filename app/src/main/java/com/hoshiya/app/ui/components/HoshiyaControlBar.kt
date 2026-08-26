package com.hoshiya.app.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoshiya.app.core.model.TimerMode
import com.hoshiya.app.core.model.TimerState
import com.hoshiya.app.core.theme.SpaceBlack
import com.hoshiya.app.core.theme.StarlightAmber
import com.hoshiya.app.core.theme.SurfaceBorder
import com.hoshiya.app.core.theme.SurfaceDarkElevated
import com.hoshiya.app.core.theme.TextPrimary
import com.hoshiya.app.core.theme.TextSecondary
import com.hoshiya.app.core.theme.TextTertiary

@Composable
fun HoshiyaControlBar(
    timerState: TimerState,
    mode: TimerMode,
    onPlayPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    onSkipClick: () -> Unit,
    onAdd5Minutes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = mode.accentColor
    val isRunning = timerState == TimerState.RUNNING

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reset Button
        IconButton(
            onClick = onResetClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                .border(1.dp, SurfaceBorder, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.Replay,
                contentDescription = "Reset Timer",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Quick +5 Min Top-up Button
        Surface(
            onClick = onAdd5Minutes,
            shape = RoundedCornerShape(16.dp),
            color = SurfaceDarkElevated.copy(alpha = 0.6f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier.height(44.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "5m",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                    color = TextPrimary
                )
            }
        }

        // Main Primary Play / Pause Button
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentColor,
                            accentColor.copy(alpha = 0.85f)
                        )
                    )
                )
                .clickable(onClick = onPlayPauseClick),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = isRunning, label = "play_pause_crossfade") { running ->
                Icon(
                    imageVector = if (running) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (running) "Pause" else "Start",
                    tint = SpaceBlack,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // Skip Button
        IconButton(
            onClick = onSkipClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                .border(1.dp, SurfaceBorder, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.SkipNext,
                contentDescription = "Skip to Next",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
