package com.hoshiya.app.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hoshiya.app.core.data.HoshiyaPreferences
import com.hoshiya.app.core.model.PomodoroSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = HoshiyaPreferences(application)

    val settings: StateFlow<PomodoroSettings> = preferences.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroSettings()
    )

    fun updateSettings(newSettings: PomodoroSettings) {
        viewModelScope.launch {
            preferences.updateSettings(newSettings)
        }
    }
}
