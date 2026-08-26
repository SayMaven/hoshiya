package com.saymaven.hoshiya.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saymaven.hoshiya.core.model.AnimeQuote
import com.saymaven.hoshiya.core.theme.SakuraPink
import com.saymaven.hoshiya.core.theme.StarlightAmber
import com.saymaven.hoshiya.core.theme.SurfaceBorder
import com.saymaven.hoshiya.core.theme.SurfaceDarkElevated
import com.saymaven.hoshiya.core.theme.TextMuted
import com.saymaven.hoshiya.core.theme.TextPrimary
import com.saymaven.hoshiya.core.theme.TextSecondary
import com.saymaven.hoshiya.core.theme.TwilightViolet

@Composable
fun AnimeCompanionCard(
    quote: AnimeQuote,
    onRefreshQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        TwilightViolet.copy(alpha = 0.3f),
                        SakuraPink.copy(alpha = 0.2f),
                        SurfaceBorder
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        color = SurfaceDarkElevated.copy(alpha = 0.85f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = StarlightAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${quote.character} \u2022 ${quote.anime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = StarlightAmber,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = onRefreshQuote,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "New Quote",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(
                targetState = quote,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "quote_text"
            ) { targetQuote ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\u300C${targetQuote.japanese}\u300D",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = targetQuote.translation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = TextPrimary.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
