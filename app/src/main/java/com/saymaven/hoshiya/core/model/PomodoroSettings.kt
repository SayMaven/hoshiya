package com.saymaven.hoshiya.core.model

data class PomodoroSettings(
    val workDurationMinutes: Int = 25,
    val shortBreakMinutes: Int = 5,
    val longBreakMinutes: Int = 15,
    val sessionsPerCycle: Int = 4,
    val autoStartBreaks: Boolean = false,
    val autoStartWork: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrateEnabled: Boolean = true,
    val keepScreenOn: Boolean = true,
    val animeQuotesEnabled: Boolean = true,
    val ambientSound: AmbientSound = AmbientSound.STARRY_NIGHT,
    val ambientVolume: Float = 0.5f
)
