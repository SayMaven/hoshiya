package com.saymaven.hoshiya.ui.timer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saymaven.hoshiya.core.audio.AmbientEngine
import com.saymaven.hoshiya.core.audio.SoundSynthesizer
import com.saymaven.hoshiya.core.data.HoshiyaPreferences
import com.saymaven.hoshiya.core.model.AmbientSound
import com.saymaven.hoshiya.core.model.AnimeQuote
import com.saymaven.hoshiya.core.model.FocusCategory
import com.saymaven.hoshiya.core.model.PomodoroSettings
import com.saymaven.hoshiya.core.model.SessionRecord
import com.saymaven.hoshiya.core.model.TimerMode
import com.saymaven.hoshiya.core.model.TimerState
import com.saymaven.hoshiya.core.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TimerUiState(
    val remainingSeconds: Int = 25 * 60,
    val totalDurationSeconds: Int = 25 * 60,
    val currentMode: TimerMode = TimerMode.WORK,
    val timerState: TimerState = TimerState.IDLE,
    val cycleIndex: Int = 0,
    val totalCycles: Int = 4,
    val selectedCategory: FocusCategory = FocusCategory.GENERAL,
    val currentQuote: AnimeQuote = AnimeQuote.randomQuote(),
    val settings: PomodoroSettings = PomodoroSettings(),
    val activeAmbientSound: AmbientSound = AmbientSound.STARRY_NIGHT,
    val ambientVolume: Float = 0.5f,
    val isAmbientPlaying: Boolean = false
)

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = HoshiyaPreferences(application)
    private val ambientEngine = AmbientEngine(application.applicationContext)

    private var timerService: TimerService? = null
    private var isBound = false

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isBound = true
            observeService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerService = null
            isBound = false
        }
    }

    init {
        bindTimerService()
        observePreferences()
    }

    private fun bindTimerService() {
        val intent = Intent(getApplication(), TimerService::class.java)
        getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun observePreferences() {
        viewModelScope.launch {
            combine(
                preferences.settingsFlow,
                preferences.selectedCategory
            ) { settings, category ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    totalCycles = settings.sessionsPerCycle,
                    selectedCategory = category,
                    activeAmbientSound = settings.ambientSound,
                    ambientVolume = settings.ambientVolume
                )
                // Sync default duration if idle
                if (_uiState.value.timerState == TimerState.IDLE) {
                    val duration = getDurationForMode(_uiState.value.currentMode, settings)
                    _uiState.value = _uiState.value.copy(
                        remainingSeconds = duration,
                        totalDurationSeconds = duration
                    )
                    timerService?.configureTimer(_uiState.value.currentMode, duration)
                }
            }.collect {}
        }
    }

    private fun observeService() {
        val service = timerService ?: return
        viewModelScope.launch {
            combine(
                service.remainingSeconds,
                service.timerState,
                service.currentMode,
                service.totalDurationSeconds
            ) { remaining, state, mode, totalDuration ->
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = remaining,
                    timerState = state,
                    currentMode = mode,
                    totalDurationSeconds = totalDuration
                )
            }.collect {}
        }

        viewModelScope.launch {
            service.sessionCompletedEvent.collect { completedMode ->
                onSessionFinished(completedMode)
            }
        }
    }

    private fun getDurationForMode(mode: TimerMode, settings: PomodoroSettings): Int {
        return when (mode) {
            TimerMode.WORK -> settings.workDurationMinutes * 60
            TimerMode.SHORT_BREAK -> settings.shortBreakMinutes * 60
            TimerMode.LONG_BREAK -> settings.longBreakMinutes * 60
        }
    }

    fun onPlayPauseClick() {
        when (_uiState.value.timerState) {
            TimerState.RUNNING -> {
                timerService?.pauseTimer()
            }
            TimerState.PAUSED -> {
                timerService?.resumeTimer()
                startAmbientIfEnabled()
            }
            TimerState.IDLE -> {
                val duration = getDurationForMode(_uiState.value.currentMode, _uiState.value.settings)
                timerService?.configureTimer(_uiState.value.currentMode, duration)
                timerService?.startTimer()
                startAmbientIfEnabled()
            }
        }
    }

    fun onResetClick() {
        timerService?.resetTimer()
        val duration = getDurationForMode(_uiState.value.currentMode, _uiState.value.settings)
        _uiState.value = _uiState.value.copy(
            remainingSeconds = duration,
            totalDurationSeconds = duration,
            timerState = TimerState.IDLE
        )
    }

    fun onSkipClick() {
        advanceToNextMode()
    }

    fun onAdd5Minutes() {
        timerService?.addMinutes(5)
    }

    fun setMode(mode: TimerMode) {
        timerService?.resetTimer()
        val duration = getDurationForMode(mode, _uiState.value.settings)
        _uiState.value = _uiState.value.copy(
            currentMode = mode,
            remainingSeconds = duration,
            totalDurationSeconds = duration,
            timerState = TimerState.IDLE
        )
        timerService?.configureTimer(mode, duration)
    }

    fun selectCategory(category: FocusCategory) {
        viewModelScope.launch {
            preferences.setCategory(category)
        }
    }

    fun refreshQuote() {
        _uiState.value = _uiState.value.copy(currentQuote = AnimeQuote.randomQuote())
    }

    fun setAmbientSound(sound: AmbientSound) {
        val newSettings = _uiState.value.settings.copy(ambientSound = sound)
        viewModelScope.launch {
            preferences.updateSettings(newSettings)
        }
        _uiState.value = _uiState.value.copy(activeAmbientSound = sound)
        if (sound == AmbientSound.OFF) {
            ambientEngine.stop()
            _uiState.value = _uiState.value.copy(isAmbientPlaying = false)
        } else {
            ambientEngine.play(sound, _uiState.value.ambientVolume)
            _uiState.value = _uiState.value.copy(isAmbientPlaying = true)
        }
    }

    fun setAmbientVolume(volume: Float) {
        val newSettings = _uiState.value.settings.copy(ambientVolume = volume)
        viewModelScope.launch {
            preferences.updateSettings(newSettings)
        }
        _uiState.value = _uiState.value.copy(ambientVolume = volume)
        ambientEngine.setVolume(volume)
    }

    private fun startAmbientIfEnabled() {
        val sound = _uiState.value.activeAmbientSound
        if (sound != AmbientSound.OFF) {
            ambientEngine.play(sound, _uiState.value.ambientVolume)
            _uiState.value = _uiState.value.copy(isAmbientPlaying = true)
        }
    }

    private fun onSessionFinished(completedMode: TimerMode) {
        val durationSecs = getDurationForMode(completedMode, _uiState.value.settings)
        viewModelScope.launch {
            preferences.recordCompletedSession(
                SessionRecord(
                    durationSeconds = durationSecs,
                    mode = completedMode,
                    category = _uiState.value.selectedCategory,
                    completed = true
                )
            )
        }
        advanceToNextMode()
    }

    private fun advanceToNextMode() {
        val currentMode = _uiState.value.currentMode
        val currentIndex = _uiState.value.cycleIndex
        val totalCycles = _uiState.value.totalCycles
        val settings = _uiState.value.settings

        var nextMode = TimerMode.WORK
        var nextIndex = currentIndex

        if (currentMode == TimerMode.WORK) {
            val newCycle = currentIndex + 1
            if (newCycle >= totalCycles) {
                nextMode = TimerMode.LONG_BREAK
                nextIndex = 0
            } else {
                nextMode = TimerMode.SHORT_BREAK
                nextIndex = newCycle
            }
        } else {
            // Finished break, back to work
            nextMode = TimerMode.WORK
        }

        val duration = getDurationForMode(nextMode, settings)
        _uiState.value = _uiState.value.copy(
            currentMode = nextMode,
            cycleIndex = nextIndex,
            remainingSeconds = duration,
            totalDurationSeconds = duration,
            timerState = TimerState.IDLE,
            currentQuote = AnimeQuote.randomQuote()
        )
        timerService?.configureTimer(nextMode, duration)

        // Auto-start check
        val shouldAutoStart = if (nextMode == TimerMode.WORK) settings.autoStartWork else settings.autoStartBreaks
        if (shouldAutoStart) {
            timerService?.startTimer()
            startAmbientIfEnabled()
        }
    }

    override fun onCleared() {
        super.onCleared()
        ambientEngine.stop()
        if (isBound) {
            try {
                getApplication<Application>().unbindService(serviceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isBound = false
        }
    }
}
