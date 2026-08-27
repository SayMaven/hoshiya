package com.saymaven.hoshiya.ui.settings

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Vibration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saymaven.hoshiya.core.model.AppThemePalette
import com.saymaven.hoshiya.core.theme.SpaceBlack
import com.saymaven.hoshiya.core.theme.StarlightAmber
import com.saymaven.hoshiya.core.theme.SurfaceBorder
import com.saymaven.hoshiya.core.theme.SurfaceDark
import com.saymaven.hoshiya.core.theme.SurfaceDarkElevated
import com.saymaven.hoshiya.core.theme.TextMuted
import com.saymaven.hoshiya.core.theme.TextPrimary
import com.saymaven.hoshiya.core.theme.TextSecondary
import com.saymaven.hoshiya.core.theme.TwilightViolet
import com.saymaven.hoshiya.ui.components.StarfieldBackground

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
        StarfieldBackground(accentColor = settings.themePalette.primaryColor)

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
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(SurfaceDarkElevated.copy(alpha = 0.6f))
                        .border(1.dp, SurfaceBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "Preferences & Durations",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.size(34.dp))
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Section: Material 3 Theme & Colors
                item {
                    SectionTitle(title = "THEME & COLOR", icon = Icons.Outlined.Palette)
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceDarkElevated.copy(alpha = 0.7f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Accent Color Palette",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(AppThemePalette.values()) { palette ->
                                    val isSelected = settings.themePalette == palette && !settings.useDynamicColor
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                viewModel.updateSettings(
                                                    settings.copy(
                                                        themePalette = palette,
                                                        useDynamicColor = false
                                                    )
                                                )
                                            }
                                            .padding(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(palette.primaryColor)
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) TextPrimary else SurfaceBorder,
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Check,
                                                    contentDescription = "Selected",
                                                    tint = SpaceBlack,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = palette.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) TextPrimary else TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Dynamic Color (Material You)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Match wallpaper colors",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Switch(
                                        checked = settings.useDynamicColor,
                                        onCheckedChange = {
                                            viewModel.updateSettings(settings.copy(useDynamicColor = it))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                            uncheckedThumbColor = TextMuted,
                                            uncheckedTrackColor = SurfaceDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Section: Timer Intervals
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionTitle(title = "TIMER INTERVALS", icon = Icons.Outlined.Timer)
                }

                // Focus Duration Slider
                item {
                    SettingSliderCard(
                        title = "Focus Time",
                        valueText = "${settings.workDurationMinutes} min",
                        value = settings.workDurationMinutes.toFloat(),
                        valueRange = 5f..90f,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(workDurationMinutes = it.toInt()))
                        }
                    )
                }

                // Short Break Slider
                item {
                    SettingSliderCard(
                        title = "Short Break",
                        valueText = "${settings.shortBreakMinutes} min",
                        value = settings.shortBreakMinutes.toFloat(),
                        valueRange = 1f..30f,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(shortBreakMinutes = it.toInt()))
                        }
                    )
                }

                // Long Break Slider
                item {
                    SettingSliderCard(
                        title = "Long Break",
                        valueText = "${settings.longBreakMinutes} min",
                        value = settings.longBreakMinutes.toFloat(),
                        valueRange = 5f..60f,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(longBreakMinutes = it.toInt()))
                        }
                    )
                }

                // Sessions Per Cycle
                item {
                    SettingSliderCard(
                        title = "Sessions per Cycle",
                        valueText = "${settings.sessionsPerCycle} sessions",
                        value = settings.sessionsPerCycle.toFloat(),
                        valueRange = 2f..8f,
                        onValueChange = {
                            viewModel.updateSettings(settings.copy(sessionsPerCycle = it.toInt()))
                        }
                    )
                }

                // Section: Automation & Experience
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionTitle(title = "AUTOMATION & BEHAVIOR", icon = Icons.Outlined.Smartphone)
                }

                item {
                    SettingSwitchCard(
                        title = "Auto-start Breaks",
                        subtitle = "Automatically start break timers after focus",
                        checked = settings.autoStartBreaks,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(autoStartBreaks = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Auto-start Focus",
                        subtitle = "Automatically start focus session after break",
                        checked = settings.autoStartWork,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(autoStartWork = it))
                        }
                    )
                }

                item {
                    SettingSwitchCard(
                        title = "Keep Screen On",
                        subtitle = "Keep display awake while timer is running",
                        checked = settings.keepScreenOn,
                        onCheckedChange = {
                            viewModel.updateSettings(settings.copy(keepScreenOn = it))
                        }
                    )
                }

                // Section: Audio & Feedback
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionTitle(title = "AUDIO & FEEDBACK", icon = Icons.Outlined.GraphicEq)
                }

                item {
                    SettingSwitchCard(
                        title = "Completion Chime",
                        subtitle = "Play gentle bell sound when timer completes",
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

                // Section: Companion
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    SectionTitle(title = "ANIME COMPANION", icon = Icons.Outlined.AutoAwesome)
                }

                item {
                    SettingSwitchCard(
                        title = "Companion Quotes",
                        subtitle = "Enable motivational quotes in records and notifications",
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
                                text = "Hoshiya v1.0.0",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Lofi Focus & Pomodoro by SayMaven",
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
private fun SectionTitle(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun SettingSliderCard(
    title: String,
    valueText: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDarkElevated.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
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
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
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
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
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
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = SurfaceDark
                )
            )
        }
    }
}
