package com.hoshiya.app.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hoshiya.app.core.model.AmbientSound
import com.hoshiya.app.core.model.FocusCategory
import com.hoshiya.app.core.model.PomodoroSettings
import com.hoshiya.app.core.model.SessionRecord
import com.hoshiya.app.core.model.TimerMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hoshiya_preferences")

class HoshiyaPreferences(private val context: Context) {

    companion object {
        private val KEY_WORK_MINUTES = intPreferencesKey("work_minutes")
        private val KEY_SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
        private val KEY_LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
        private val KEY_SESSIONS_PER_CYCLE = intPreferencesKey("sessions_per_cycle")
        private val KEY_AUTO_START_BREAKS = booleanPreferencesKey("auto_start_breaks")
        private val KEY_AUTO_START_WORK = booleanPreferencesKey("auto_start_work")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val KEY_VIBRATE_ENABLED = booleanPreferencesKey("vibrate_enabled")
        private val KEY_KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        private val KEY_ANIME_QUOTES = booleanPreferencesKey("anime_quotes")
        private val KEY_AMBIENT_SOUND = stringPreferencesKey("ambient_sound")
        private val KEY_AMBIENT_VOLUME = floatPreferencesKey("ambient_volume")

        private val KEY_TOTAL_FOCUS_SECONDS = longPreferencesKey("total_focus_seconds")
        private val KEY_TOTAL_SESSIONS = intPreferencesKey("total_sessions")
        private val KEY_CURRENT_STREAK = intPreferencesKey("current_streak")
        private val KEY_LAST_ACTIVE_DATE = stringPreferencesKey("last_active_date")
        private val KEY_SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        private val KEY_SESSION_HISTORY = stringPreferencesKey("session_history")
    }

    val settingsFlow: Flow<PomodoroSettings> = context.dataStore.data.map { prefs ->
        PomodoroSettings(
            workDurationMinutes = prefs[KEY_WORK_MINUTES] ?: 25,
            shortBreakMinutes = prefs[KEY_SHORT_BREAK_MINUTES] ?: 5,
            longBreakMinutes = prefs[KEY_LONG_BREAK_MINUTES] ?: 15,
            sessionsPerCycle = prefs[KEY_SESSIONS_PER_CYCLE] ?: 4,
            autoStartBreaks = prefs[KEY_AUTO_START_BREAKS] ?: false,
            autoStartWork = prefs[KEY_AUTO_START_WORK] ?: false,
            soundEnabled = prefs[KEY_SOUND_ENABLED] ?: true,
            vibrateEnabled = prefs[KEY_VIBRATE_ENABLED] ?: true,
            keepScreenOn = prefs[KEY_KEEP_SCREEN_ON] ?: true,
            animeQuotesEnabled = prefs[KEY_ANIME_QUOTES] ?: true,
            ambientSound = AmbientSound.fromName(prefs[KEY_AMBIENT_SOUND]),
            ambientVolume = prefs[KEY_AMBIENT_VOLUME] ?: 0.5f
        )
    }

    val totalFocusSeconds: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[KEY_TOTAL_FOCUS_SECONDS] ?: 0L
    }

    val totalSessions: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_TOTAL_SESSIONS] ?: 0
    }

    val currentStreak: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_CURRENT_STREAK] ?: 0
    }

    val selectedCategory: Flow<FocusCategory> = context.dataStore.data.map { prefs ->
        FocusCategory.fromName(prefs[KEY_SELECTED_CATEGORY])
    }

    val sessionHistory: Flow<List<SessionRecord>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_SESSION_HISTORY] ?: ""
        parseSessions(raw)
    }

    suspend fun updateSettings(settings: PomodoroSettings) {
        context.dataStore.edit { prefs ->
            prefs[KEY_WORK_MINUTES] = settings.workDurationMinutes
            prefs[KEY_SHORT_BREAK_MINUTES] = settings.shortBreakMinutes
            prefs[KEY_LONG_BREAK_MINUTES] = settings.longBreakMinutes
            prefs[KEY_SESSIONS_PER_CYCLE] = settings.sessionsPerCycle
            prefs[KEY_AUTO_START_BREAKS] = settings.autoStartBreaks
            prefs[KEY_AUTO_START_WORK] = settings.autoStartWork
            prefs[KEY_SOUND_ENABLED] = settings.soundEnabled
            prefs[KEY_VIBRATE_ENABLED] = settings.vibrateEnabled
            prefs[KEY_KEEP_SCREEN_ON] = settings.keepScreenOn
            prefs[KEY_ANIME_QUOTES] = settings.animeQuotesEnabled
            prefs[KEY_AMBIENT_SOUND] = settings.ambientSound.name
            prefs[KEY_AMBIENT_VOLUME] = settings.ambientVolume
        }
    }

    suspend fun setCategory(category: FocusCategory) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_CATEGORY] = category.name
        }
    }

    suspend fun recordCompletedSession(record: SessionRecord) {
        context.dataStore.edit { prefs ->
            if (record.mode == TimerMode.WORK) {
                val currentSecs = prefs[KEY_TOTAL_FOCUS_SECONDS] ?: 0L
                prefs[KEY_TOTAL_FOCUS_SECONDS] = currentSecs + record.durationSeconds

                val currentCount = prefs[KEY_TOTAL_SESSIONS] ?: 0
                prefs[KEY_TOTAL_SESSIONS] = currentCount + 1

                // Streak logic
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val lastDate = prefs[KEY_LAST_ACTIVE_DATE] ?: ""
                val currentStreak = prefs[KEY_CURRENT_STREAK] ?: 0

                if (lastDate != todayStr) {
                    val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date(System.currentTimeMillis() - 86400000L))
                    if (lastDate == yesterdayStr) {
                        prefs[KEY_CURRENT_STREAK] = currentStreak + 1
                    } else if (lastDate.isEmpty()) {
                        prefs[KEY_CURRENT_STREAK] = 1
                    } else {
                        prefs[KEY_CURRENT_STREAK] = 1
                    }
                    prefs[KEY_LAST_ACTIVE_DATE] = todayStr
                }
            }

            // Append history
            val existing = prefs[KEY_SESSION_HISTORY] ?: ""
            val encoded = "${record.id},${record.timestamp},${record.durationSeconds},${record.mode.name},${record.category.name},${record.completed}"
            val updated = if (existing.isEmpty()) encoded else "$encoded;$existing"
            // Keep last 100 entries to prevent growth
            val trimmed = updated.split(";").take(100).joinToString(";")
            prefs[KEY_SESSION_HISTORY] = trimmed
        }
    }

    private fun parseSessions(raw: String): List<SessionRecord> {
        if (raw.isBlank()) return emptyList()
        return raw.split(";").mapNotNull { entry ->
            val parts = entry.split(",")
            if (parts.size >= 6) {
                try {
                    SessionRecord(
                        id = parts[0].toLong(),
                        timestamp = parts[1].toLong(),
                        durationSeconds = parts[2].toInt(),
                        mode = TimerMode.valueOf(parts[3]),
                        category = FocusCategory.fromName(parts[4]),
                        completed = parts[5].toBoolean()
                    )
                } catch (e: Exception) {
                    null
                }
            } else null
        }
    }
}
