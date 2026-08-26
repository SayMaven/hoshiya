package com.hoshiya.app.core.model

import androidx.compose.ui.graphics.Color
import com.hoshiya.app.core.theme.LongBreakAccent
import com.hoshiya.app.core.theme.ShortBreakAccent
import com.hoshiya.app.core.theme.WorkAccent

enum class TimerMode(
    val title: String,
    val subtitle: String,
    val japaneseTag: String,
    val defaultMinutes: Int
) {
    WORK("Focus", "Stay in the zone", "集中", 25),
    SHORT_BREAK("Short Break", "Breathe & stretch", "小休憩", 5),
    LONG_BREAK("Long Break", "Relax & recharge", "大休憩", 15);

    val accentColor: Color
        get() = when (this) {
            WORK -> WorkAccent
            SHORT_BREAK -> ShortBreakAccent
            LONG_BREAK -> LongBreakAccent
        }
}
