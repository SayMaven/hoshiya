package com.saymaven.hoshiya.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saymaven.hoshiya.core.model.TimerMode
import com.saymaven.hoshiya.core.theme.SpaceBlack
import com.saymaven.hoshiya.core.theme.SurfaceBorder
import com.saymaven.hoshiya.core.theme.SurfaceDark
import com.saymaven.hoshiya.core.theme.SurfaceDarkElevated
import com.saymaven.hoshiya.core.theme.TextSecondary
import com.saymaven.hoshiya.core.theme.TwilightViolet
import com.saymaven.hoshiya.ui.ambient.AmbientBottomSheet
import com.saymaven.hoshiya.ui.components.CelestialTimer
import com.saymaven.hoshiya.ui.components.StarfieldBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAmbientSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var totalDragX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { totalDragX = 0f },
                    onDragEnd = {
                        if (totalDragX > 120f) {
                            // Swipe right -> previous mode
                            when (uiState.currentMode) {
                                TimerMode.LONG_BREAK -> viewModel.setMode(TimerMode.SHORT_BREAK)
                                TimerMode.SHORT_BREAK -> viewModel.setMode(TimerMode.WORK)
                                TimerMode.WORK -> {}
                            }
                        } else if (totalDragX < -120f) {
                            // Swipe left -> next mode
                            when (uiState.currentMode) {
                                TimerMode.WORK -> viewModel.setMode(TimerMode.SHORT_BREAK)
                                TimerMode.SHORT_BREAK -> viewModel.setMode(TimerMode.LONG_BREAK)
                                TimerMode.LONG_BREAK -> {}
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        totalDragX += dragAmount
                    }
                )
            }
    ) {
        // Shimmering Starfield Background
        StarfieldBackground(accentColor = uiState.currentMode.accentColor)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Top Bar: Only 3 Action Buttons on the Top Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Ambient Sound Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (uiState.isAmbientPlaying) TwilightViolet.copy(alpha = 0.25f)
                                else SurfaceDarkElevated.copy(alpha = 0.6f)
                            )
                            .border(
                                1.dp,
                                if (uiState.isAmbientPlaying) TwilightViolet.copy(alpha = 0.6f) else SurfaceBorder,
                                CircleShape
                            )
                            .clickable(onClick = { showAmbientSheet = true }),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (uiState.isAmbientPlaying) Icons.Filled.GraphicEq else Icons.Outlined.GraphicEq,
                            contentDescription = "Ambient Sounds",
                            tint = if (uiState.isAmbientPlaying) TwilightViolet else TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Stats Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                            .border(1.dp, SurfaceBorder, CircleShape)
                            .clickable(onClick = onNavigateToStats),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BarChart,
                            contentDescription = "Focus Records",
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Settings Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                            .border(1.dp, SurfaceBorder, CircleShape)
                            .clickable(onClick = onNavigateToSettings),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Center: Circular Timer
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CelestialTimer(
                    remainingSeconds = uiState.remainingSeconds,
                    totalDurationSeconds = uiState.totalDurationSeconds,
                    mode = uiState.currentMode,
                    timerState = uiState.timerState,
                    onTimerClick = { viewModel.onPlayPauseClick() }
                )
            }
        }

        // Ambient Sound Sheet
        if (showAmbientSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAmbientSheet = false },
                sheetState = sheetState,
                containerColor = SurfaceDark,
                dragHandle = null
            ) {
                AmbientBottomSheet(
                    currentSound = uiState.activeAmbientSound,
                    volume = uiState.ambientVolume,
                    onSelectSound = { viewModel.setAmbientSound(it) },
                    onVolumeChange = { viewModel.setAmbientVolume(it) },
                    onClose = { showAmbientSheet = false }
                )
            }
        }
    }
}
