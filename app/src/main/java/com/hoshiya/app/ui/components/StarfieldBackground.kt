package com.hoshiya.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.hoshiya.app.core.theme.DeepMidnight
import com.hoshiya.app.core.theme.SpaceBlack
import com.hoshiya.app.core.theme.StarPurple
import java.util.Random

private data class StarParticle(
    val xRatio: Float,
    val yRatio: Float,
    val radius: Float,
    val alphaBase: Float,
    val twinkleSpeed: Int,
    val color: Color
)

@Composable
fun StarfieldBackground(
    modifier: Modifier = Modifier,
    accentColor: Color = StarPurple
) {
    val transition = rememberInfiniteTransition(label = "star_twinkle")
    val twinkleFactor = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    val stars = remember {
        val random = Random(42)
        val list = mutableListOf<StarParticle>()
        val starColors = listOf(
            Color(0xFFFDE49E),
            Color(0xFFFFFFFF),
            Color(0xFF80FFDB),
            Color(0xFFF3A6C8),
            Color(0xFF8E8FFA)
        )
        for (i in 0 until 65) {
            list.add(
                StarParticle(
                    xRatio = random.nextFloat(),
                    yRatio = random.nextFloat(),
                    radius = random.nextFloat() * 1.8f + 0.8f,
                    alphaBase = random.nextFloat() * 0.5f + 0.3f,
                    twinkleSpeed = random.nextInt(3) + 1,
                    color = starColors[random.nextInt(starColors.size)]
                )
            )
        }
        list
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Deep cosmic radial gradient
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0.08f),
                    DeepMidnight,
                    SpaceBlack
                ),
                center = Offset(width * 0.5f, height * 0.35f),
                radius = width * 0.9f
            )
        )

        // Draw star particles
        stars.forEachIndexed { index, star ->
            val phase = (twinkleFactor.value * star.twinkleSpeed + index * 0.1f) % 1.0f
            val alpha = (star.alphaBase * (0.5f + 0.5f * phase)).coerceIn(0.1f, 0.95f)

            drawCircle(
                color = star.color.copy(alpha = alpha),
                radius = star.radius,
                center = Offset(star.xRatio * width, star.yRatio * height)
            )
        }
    }
}
