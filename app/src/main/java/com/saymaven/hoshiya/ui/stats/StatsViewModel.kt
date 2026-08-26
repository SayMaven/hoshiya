package com.saymaven.hoshiya.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saymaven.hoshiya.core.data.HoshiyaPreferences
import com.saymaven.hoshiya.core.model.FocusCategory
import com.saymaven.hoshiya.core.model.SessionRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class StatsUiState(
    val totalFocusSeconds: Long = 0L,
    val totalSessions: Int = 0,
    val currentStreak: Int = 0,
    val sessionsHistory: List<SessionRecord> = emptyList(),
    val categoryBreakdown: Map<FocusCategory, Int> = emptyMap()
) {
    val totalHours: Float
        get() = totalFocusSeconds / 3600f

    val focusRank: String
        get() = when {
            totalHours < 2f -> "Novice (見習い昁E"
            totalHours < 10f -> "Nebula Scholar (星雲の学老E"
            totalHours < 30f -> "Constellation Knight (星座の騎士)"
            totalHours < 80f -> "Astral Sage (星界の賢老E"
            else -> "Cosmic Master (星王)"
        }
}

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = HoshiyaPreferences(application)

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferences.totalFocusSeconds,
                preferences.totalSessions,
                preferences.currentStreak,
                preferences.sessionHistory
            ) { totalSecs, totalSessions, streak, history ->
                val breakdown = history.groupBy { it.category }
                    .mapValues { it.value.sumOf { s -> s.durationSeconds / 60 } }

                StatsUiState(
                    totalFocusSeconds = totalSecs,
                    totalSessions = totalSessions,
                    currentStreak = streak,
                    sessionsHistory = history,
                    categoryBreakdown = breakdown
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
