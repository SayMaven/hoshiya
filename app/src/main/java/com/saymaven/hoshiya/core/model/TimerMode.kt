package com.saymaven.hoshiya.core.model

import androidx.compose.ui.graphics.Color
import com.saymaven.hoshiya.core.theme.LongBreakAccent
import com.saymaven.hoshiya.core.theme.ShortBreakAccent
import com.saymaven.hoshiya.core.theme.WorkAccent

enum class TimerMode(
    val title: String,
    val subtitle: String,
    val japaneseTag: String,
    val defaultMinutes: Int
) {
    WORK("Focus", "Stay in the zone", "é›E¸­", 25),
    SHORT_BREAK("Short Break", "Breathe & stretch", "å°ä¼‘æE", 5),
    LONG_BREAK("Long Break", "Relax & recharge", "å¤§ä¼‘æE", 15);

    val accentColor: Color
        get() = when (this) {
            WORK -> WorkAccent
            SHORT_BREAK -> ShortBreakAccent
            LONG_BREAK -> LongBreakAccent
        }
}
