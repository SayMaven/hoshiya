package com.saymaven.hoshiya.core.model

enum class AmbientSound(
    val title: String,
    val japaneseTitle: String,
    val description: String
) {
    OFF("Mute", "無音", "Silence for pure focus"),
    STARRY_NIGHT("Starry Night", "星空", "Gentle cosmic ambient frequency"),
    LOFI_RAIN("Rainfall", "雨音", "Soft rain ambience"),
    COZY_ROOM("Quiet Room", "和室", "Warm ambient tone"),
    MIDNIGHT_CAFE("Midnight Cafe", "夜カフェ", "Mellow acoustic hum");

    companion object {
        fun fromName(name: String?): AmbientSound {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: OFF
        }
    }
}
