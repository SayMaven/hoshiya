package com.hoshiya.app.core.model

enum class AmbientSound(
    val title: String,
    val japaneseTitle: String,
    val description: String,
    val emoji: String
) {
    OFF("Mute", "無音", "Silence for pure focus", "🔇"),
    STARRY_NIGHT("Starry Night", "星空", "Gentle cosmic ambient drone", "✨"),
    LOFI_RAIN("Lofi Rain", "雨音", "Soft rain patter on tatami", "🌧️"),
    COZY_ROOM("Cozy Room", "和室", "Warm crackling ambient warmth", "🍵"),
    MIDNIGHT_CAFE("Midnight Cafe", "夜カフェ", "Mellow acoustic cafe hum", "☕");

    companion object {
        fun fromName(name: String?): AmbientSound {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: OFF
        }
    }
}
