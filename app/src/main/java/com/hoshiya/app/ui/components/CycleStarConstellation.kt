package com.hoshiya.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hoshiya.app.core.theme.StarlightAmber
import com.hoshiya.app.core.theme.SurfaceBorder
import com.hoshiya.app.core.theme.TextTertiary

@Composable
fun CycleStarConstellation(
    currentCycleIndex: Int,
    totalCycles: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_active_star")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_scale"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalCycles) {
            val isCompleted = i < currentCycleIndex
            val isCurrent = i == currentCycleIndex

            val starColor by animateColorAsState(
                targetValue = when {
                    isCompleted -> StarlightAmber
                    isCurrent -> StarlightAmber.copy(alpha = 0.9f)
                    else -> TextTertiary.copy(alpha = 0.4f)
                },
                label = "star_color"
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .then(
                        if (isCurrent) Modifier.scale(pulseScale) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted || isCurrent) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Cycle ${i + 1}",
                        tint = starColor,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = "Cycle ${i + 1}",
                        tint = starColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
