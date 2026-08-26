package com.saymaven.hoshiya.core.model

data class SessionRecord(
    val id: Long = System.currentTimeMillis(),
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val mode: TimerMode,
    val category: FocusCategory,
    val completed: Boolean = true
)
