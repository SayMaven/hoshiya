package com.hoshiya.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoshiya.app.core.theme.SpaceBlack
import com.hoshiya.app.core.theme.StarlightAmber
import com.hoshiya.app.core.theme.SurfaceBorder
import com.hoshiya.app.core.theme.SurfaceDark
import com.hoshiya.app.core.theme.SurfaceDarkElevated
import com.hoshiya.app.core.theme.TextMuted
import com.hoshiya.app.core.theme.TextPrimary
import com.hoshiya.app.core.theme.TextSecondary
import com.hoshiya.app.core.theme.TwilightViolet
import com.hoshiya.app.ui.components.StarfieldBackground

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {
        StarfieldBackground(accentColor = TwilightViolet)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                        .border(1.dp, SurfaceBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Timer Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "設定 • Durations & Preferences",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.size(38.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Section: Intervals
                item {
                    Text(
                        text = "TIMER INTERVALS",
                        style = MaterialTheme.typography.labelSmall,
                        color = StarlightAmber,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Focus Duration Slider
                item {
                    SettingSliderCard(
                        title = "Focus Time (集中)",
                        valueText = "${settings.workDurationMinutes} min",
                        value = settings.workDurationMinutes.toFloat(),
                        valueRange = 5f..90f,
                        steps = 16,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(workDurationMinutes = it.toInt()))
                        }
                    )
                }

                // Short Break Slider
                item {
                    SettingSliderCard(
                        title = "Short Break (小休憩)",
                        valueText = "${settings.shortBreakMinutes} min",
                        value = settings.shortBreakMinutes.toFloat(),
                        valueRange = 1f..30f,
                        steps = 28,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(shortBreakMinutes = it.toInt()))
                        }
                    )
                }

                // Long Break Slider
                item {
                    SettingSliderCard(
                        title = "Long Break (大休憩)",
                        valueText = "${settings.longBreakMinutes} min",
                        value = settings.longBreakMinutes.toFloat(),
                        valueRange = 5f..60f,
                        steps = 10,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(longBreakMinutes = it.toInt()))
                        }
                    )
                }

                // Sessions Per Cycle
                item {
                    SettingSliderCard(
                        title = "Sessions per Cycle (サイクル)",
                        valueText = "${settings.sessionsPerCycle} stars",
                        value = settings.sessionsPerCycle.toFloat(),
                        valueRange = 2f..8f,
                        steps = 5,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(sessionsPerCycle = it.toInt()))
                        }
                    )
                }

                // Section: Automation & Sound
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "AUTOMATION & EXPERIENCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = StarlightAmber,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Auto-start Breaks",
                        subtitle = "Automatically begin break timers after focus",
                        checked = settings.autoStartBreaks,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(autoStartBreaks = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Auto-start Next Focus",
                        subtitle = "Automatically begin focus session after break",
                        checked = settings.autoStartWork,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(autoStartWork = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Celestial Chime & Sound",
                        subtitle = "Play gentle bell notification when timer finishes",
                        checked = settings.soundEnabled,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(soundEnabled = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Haptic Vibration",
                        subtitle = "Vibrate softly on timer completion",
                        checked = settings.vibrateEnabled,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(vibrateEnabled = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Keep Screen On",
                        subtitle = "Prevent screen timeout while timer is running",
                        checked = settings.keepScreenOn,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(keepScreenOn = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Anime Companion Quotes",
                        subtitle = "Show motivating anime quotes on timer screen",
                        checked = settings.animeQuotesEnabled,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(animeQuotesEnabled = it))
                        }
                    )
                }

                // About section
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceDarkElevated.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Hoshiya 星夜 v1.0.0",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Lofi Anime Focus & Pomodoro for Weebs ✨",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingSliderCard(
    title: String,
    valueText: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDarkElevated.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.labelLarge,
                    color = TwilightViolet,
                    fontWeight = FontWeight.Bold
                )
            }
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
                colors = SliderDefaults.colors(
                    thumbColor = TwilightViolet,
                    activeTrackColor = TwilightViolet,
                    inactiveTrackColor = SurfaceBorder
                )
            )
        }
    }
}

@Composable
private fun SettingSwitchCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDarkElevated.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TwilightViolet,
                    checkedTrackColor = TwilightViolet.copy(alpha = 0.3f),
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = SurfaceDark
                )
            )
        }
    }
}
