package com.saymaven.hoshiya.ui.ambient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Check
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
import com.saymaven.hoshiya.core.theme.SurfaceBorder
import com.saymaven.hoshiya.core.theme.SurfaceDarkElevated
import com.saymaven.hoshiya.core.theme.TextPrimary
import com.saymaven.hoshiya.core.theme.TextSecondary

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
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 18.dp)
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
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Calming soundscapes for deep focus",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(SurfaceDarkElevated.copy(alpha = 0.8f))
                    .border(1.dp, SurfaceBorder, CircleShape)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sound Cards List
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AmbientSound.values().forEach { sound ->
                val isSelected = currentSound == sound
                Surface(
                    onClick = { onSelectSound(sound) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) primaryColor.copy(alpha = 0.12f) else SurfaceDarkElevated.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) primaryColor else SurfaceBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) primaryColor.copy(alpha = 0.2f)
                                        else SurfaceDarkElevated
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getAmbientIcon(sound),
                                    contentDescription = null,
                                    tint = if (isSelected) primaryColor else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = sound.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isSelected) primaryColor else TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = sound.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = "Active",
                                    tint = androidx.compose.ui.graphics.Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Volume Control Slider (Only active if not mute)
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
                        text = "Ambiance Volume",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(volume * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeDown,
                        contentDescription = "Min Volume",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Slider(
                        value = volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = primaryColor,
                            activeTrackColor = primaryColor,
                            inactiveTrackColor = SurfaceBorder
                        )
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        contentDescription = "Max Volume",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
