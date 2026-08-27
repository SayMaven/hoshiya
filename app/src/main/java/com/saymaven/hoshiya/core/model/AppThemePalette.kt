package com.saymaven.hoshiya.core.model

import androidx.compose.ui.graphics.Color
import com.saymaven.hoshiya.core.theme.NebulaTeal
import com.saymaven.hoshiya.core.theme.SakuraPink
import com.saymaven.hoshiya.core.theme.StarlightAmber
import com.saymaven.hoshiya.core.theme.TwilightViolet

enum class AppThemePalette(
    val title: String,
    val primaryColor: Color
) {
    CELESTIAL("Celestial", TwilightViolet),
    SAKURA("Sakura", SakuraPink),
    NEBULA("Nebula", NebulaTeal),
    AMBER("Starlight", StarlightAmber),
    AMETHYST("Amethyst", Color(0xFFB588F7)),
    MONOCHROME("Monochrome", Color(0xFFE2E8F0));

    companion object {
        fun fromName(name: String?): AppThemePalette {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: CELESTIAL
        }
    }
}
