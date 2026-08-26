package com.hoshiya.app.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoshiya.app.core.model.AmbientSound
import com.hoshiya.app.core.model.FocusCategory
import com.hoshiya.app.core.model.TimerMode
import com.hoshiya.app.core.theme.SakuraPink
import com.hoshiya.app.core.theme.SpaceBlack
import com.hoshiya.app.core.theme.StarlightAmber
import com.hoshiya.app.core.theme.SurfaceBorder
import com.hoshiya.app.core.theme.SurfaceDark
import com.hoshiya.app.core.theme.SurfaceDarkElevated
import com.hoshiya.app.core.theme.TextMuted
import com.hoshiya.app.core.theme.TextPrimary
import com.hoshiya.app.core.theme.TextSecondary
import com.hoshiya.app.core.theme.TwilightViolet
import com.hoshiya.app.ui.ambient.AmbientBottomSheet
import com.hoshiya.app.ui.components.AnimeCompanionCard
import com.hoshiya.app.ui.components.CelestialTimer
import com.hoshiya.app.ui.components.CycleStarConstellation
import com.hoshiya.app.ui.components.HoshiyaControlBar
import com.hoshiya.app.ui.components.StarfieldBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }
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
                            // Swiped right -> previous mode
                            when (uiState.currentMode) {
                                TimerMode.LONG_BREAK -> viewModel.setMode(TimerMode.SHORT_BREAK)
                                TimerMode.SHORT_BREAK -> viewModel.setMode(TimerMode.WORK)
                                TimerMode.WORK -> {}
                            }
                        } else if (totalDragX < -120f) {
                            // Swiped left -> next mode
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Branding, Category & Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip Picker
                Surface(
                    onClick = { showCategoryDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceDarkElevated.copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = uiState.selectedCategory.emoji, fontSize = 14.sp)
                        Text(
                            text = uiState.selectedCategory.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = TextPrimary
                        )
                    }
                }

                // Action Buttons (Ambient Audio, Stats, Settings)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Ambient Audio Button
                    IconButton(
                        onClick = { showAmbientSheet = true },
                        modifier = Modifier
                            .size(38.dp)
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
                    ) {
                        Icon(
                            imageVector = if (uiState.isAmbientPlaying) Icons.Filled.GraphicEq else Icons.Outlined.GraphicEq,
                            contentDescription = "Ambient Sounds",
                            tint = if (uiState.isAmbientPlaying) TwilightViolet else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Stats Button
                    IconButton(
                        onClick = onNavigateToStats,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                            .border(1.dp, SurfaceBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BarChart,
                            contentDescription = "Focus Stats",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Settings Button
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                            .border(1.dp, SurfaceBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Mode Selector Tabs (Focus, Short Break, Long Break)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TimerMode.values().forEach { mode ->
                    val isSelected = uiState.currentMode == mode
                    val tabColor by animateColorAsState(
                        targetValue = if (isSelected) mode.accentColor.copy(alpha = 0.18f) else Color.Transparent,
                        label = "tab_color"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) mode.accentColor else TextSecondary,
                        label = "tab_text_color"
                    )

                    Surface(
                        onClick = { viewModel.setMode(mode) },
                        shape = RoundedCornerShape(16.dp),
                        color = tabColor,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = mode.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Celestial Circular Timer (Tap to play/pause)
            CelestialTimer(
                remainingSeconds = uiState.remainingSeconds,
                totalDurationSeconds = uiState.totalDurationSeconds,
                mode = uiState.currentMode,
                timerState = uiState.timerState,
                onTimerClick = { viewModel.onPlayPauseClick() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Cycle Star Constellation Progress Dots
            CycleStarConstellation(
                currentCycleIndex = uiState.cycleIndex,
                totalCycles = uiState.totalCycles
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Anime Companion Quote Card
            if (uiState.settings.animeQuotesEnabled) {
                AnimeCompanionCard(
                    quote = uiState.currentQuote,
                    onRefreshQuote = { viewModel.refreshQuote() }
                )
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Minimalist Controls
            HoshiyaControlBar(
                timerState = uiState.timerState,
                mode = uiState.currentMode,
                onPlayPauseClick = { viewModel.onPlayPauseClick() },
                onResetClick = { viewModel.onResetClick() },
                onSkipClick = { viewModel.onSkipClick() },
                onAdd5Minutes = { viewModel.onAdd5Minutes() }
            )
        }

        // Category Picker Dialog
        if (showCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showCategoryDialog = false },
                containerColor = SurfaceDark,
                title = {
                    Text(
                        text = "✨ Select Focus Category",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        FocusCategory.values().forEach { category ->
                            val isSelected = uiState.selectedCategory == category
                            Surface(
                                onClick = {
                                    viewModel.selectCategory(category)
                                    showCategoryDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) TwilightViolet.copy(alpha = 0.2f) else SurfaceDarkElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) TwilightViolet else SurfaceBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(text = category.emoji, fontSize = 20.sp)
                                    Text(
                                        text = category.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isSelected) TwilightViolet else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCategoryDialog = false }) {
                        Text(text = "Close", color = TwilightViolet)
                    }
                }
            )
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
