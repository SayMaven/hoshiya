package com.saymaven.hoshiya.ui.ambient

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeDown
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saymaven.hoshiya.core.model.AmbientSound
import com.saymaven.hoshiya.core.theme.SakuraPink
import com.saymaven.hoshiya.core.theme.StarlightAmber
import com.saymaven.hoshiya.core.theme.SurfaceBorder
import com.saymaven.hoshiya.core.theme.SurfaceDarkElevated
import com.saymaven.hoshiya.core.theme.TextMuted
import com.saymaven.hoshiya.core.theme.TextPrimary
import com.saymaven.hoshiya.core.theme.TextSecondary
import com.saymaven.hoshiya.core.theme.TwilightViolet

fun getAmbientIcon(sound: AmbientSound): ImageVector {
    return when (sound) {
        AmbientSound.OFF -> Icons.AutoMirrored.Outlined.VolumeOff
        AmbientSound.STARRY_NIGHT -> Icons.Outlined.Nightlight
        AmbientSound.LOFI_RAIN -> Icons.Outlined.WaterDrop
        AmbientSound.COZY_ROOM -> Icons.Outlined.Home
        AmbientSound.MIDNIGHT_CAFE -> Icons.Outlined.LocalCafe
    }
}

@Composable
fun AmbientBottomSheet(
    currentSound: AmbientSound,
    volume: Float,
    onSelectSound: (AmbientSound) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Lofi Ambiance",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = "Procedural soundscapes for focus",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, SurfaceBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Sound Cards List
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AmbientSound.values().forEach { sound ->
                val isSelected = currentSound == sound
                Surface(
                    onClick = { onSelectSound(sound) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) TwilightViolet.copy(alpha = 0.16f) else SurfaceDarkElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) TwilightViolet else SurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(
                                imageVector = getAmbientIcon(sound),
                                contentDescription = null,
                                tint = if (isSelected) TwilightViolet else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = sound.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) TwilightViolet else TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "(${sound.japaneseTitle})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) SakuraPink else TextMuted
                                    )
                                }
                                Text(
                                    text = sound.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (isSelected) {
                            Text(
                                text = "ACTIVE",
                                style = MaterialTheme.typography.labelSmall,
                                color = StarlightAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Volume Control Slider
        if (currentSound != AmbientSound.OFF) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ambiance Volume",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "${(volume * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = TwilightViolet,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeDown,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Slider(
                        value = volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = TwilightViolet,
                            activeTrackColor = TwilightViolet,
                            inactiveTrackColor = SurfaceBorder
                        )
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
