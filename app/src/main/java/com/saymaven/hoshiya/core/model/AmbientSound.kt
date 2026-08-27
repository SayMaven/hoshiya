package com.saymaven.hoshiya.core.model

import androidx.annotation.RawRes
import com.saymaven.hoshiya.R

enum class AmbientSound(
    val title: String,
    val description: String,
    @RawRes val resId: Int?
) {
    OFF("Mute", "Silence for distraction-free focus", null),
    STARRY_NIGHT("Starry Night", "Celestial cosmic synth pads & gentle sparkle", R.raw.ambient_starry),
    LOFI_RAIN("Lofi Rain", "Soothing rainfall & window droplet impacts", R.raw.ambient_rain),
    COZY_ROOM("Cozy Hearth", "Warm vinyl record crackle & fireplace embers", R.raw.ambient_cozy),
    MIDNIGHT_CAFE("Midnight Cafe", "Lofi Rhodes electric piano jazz chords", R.raw.ambient_cafe);

    companion object {
        fun fromName(name: String?): AmbientSound {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: OFF
        }
    }
}
