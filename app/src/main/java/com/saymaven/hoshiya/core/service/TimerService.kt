package com.saymaven.hoshiya.core.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.saymaven.hoshiya.HoshiyaApplication
import com.saymaven.hoshiya.MainActivity
import com.saymaven.hoshiya.R
import com.saymaven.hoshiya.core.audio.SoundSynthesizer
import com.saymaven.hoshiya.core.model.TimerMode
import com.saymaven.hoshiya.core.model.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val binder = TimerBinder()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null

    private val _remainingSeconds = MutableStateFlow(25 * 60)
    val remainingSeconds = _remainingSeconds.asStateFlow()

    private val _totalDurationSeconds = MutableStateFlow(25 * 60)
    val totalDurationSeconds = _totalDurationSeconds.asStateFlow()

    private val _currentMode = MutableStateFlow(TimerMode.WORK)
    val currentMode = _currentMode.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState = _timerState.asStateFlow()

    private val _sessionCompletedEvent = MutableSharedFlow<TimerMode>()
    val sessionCompletedEvent = _sessionCompletedEvent.asSharedFlow()

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Hoshiya:TimerWakeLock")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTimer()
            ACTION_PAUSE -> pauseTimer()
            ACTION_RESUME -> resumeTimer()
            ACTION_RESET -> resetTimer()
            ACTION_SKIP -> skipTimer()
            ACTION_STOP_SERVICE -> stopServiceInternal()
        }
        return START_STICKY
    }

    fun configureTimer(mode: TimerMode, durationSeconds: Int) {
        if (_timerState.value != TimerState.RUNNING) {
            _currentMode.value = mode
            _totalDurationSeconds.value = durationSeconds
            _remainingSeconds.value = durationSeconds
            _timerState.value = TimerState.IDLE
        }
    }

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return
        _timerState.value = TimerState.RUNNING
        wakeLock?.acquire(3600000L) // Max 1 hour safety

        startForegroundInternal()

        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive && _remainingSeconds.value > 0) {
                delay(1000L)
                _remainingSeconds.value -= 1
                updateNotification()
            }

            if (_remainingSeconds.value <= 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        if (_timerState.value == TimerState.RUNNING) {
            timerJob?.cancel()
            _timerState.value = TimerState.PAUSED
            wakeLock?.takeIf { it.isHeld }?.release()
            updateNotification()
        }
    }

    fun resumeTimer() {
        if (_timerState.value == TimerState.PAUSED) {
            startTimer()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _remainingSeconds.value = _totalDurationSeconds.value
        _timerState.value = TimerState.IDLE
        wakeLock?.takeIf { it.isHeld }?.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    fun skipTimer() {
        timerJob?.cancel()
        scope.launch {
            _sessionCompletedEvent.emit(_currentMode.value)
        }
        resetTimer()
    }

    fun addMinutes(minutes: Int) {
        val extra = minutes * 60
        _remainingSeconds.value += extra
        _totalDurationSeconds.value += extra
        updateNotification()
    }

    private fun onTimerFinished() {
        _timerState.value = TimerState.IDLE
        wakeLock?.takeIf { it.isHeld }?.release()
        
        // Audio & Haptic cues
        SoundSynthesizer.playCompletionChime()
        SoundSynthesizer.vibrateGentle(this)

        scope.launch {
            _sessionCompletedEvent.emit(_currentMode.value)
        }

        updateNotificationComplete()
    }

    private fun startForegroundInternal() {
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                HoshiyaApplication.TIMER_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(HoshiyaApplication.TIMER_NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification() {
        val notification = buildNotification()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(HoshiyaApplication.TIMER_NOTIFICATION_ID, notification)
    }

    private fun updateNotificationComplete() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val modeTitle = _currentMode.value.title
        val notification = NotificationCompat.Builder(this, HoshiyaApplication.TIMER_CHANNEL_ID)
            .setContentTitle("✨ $modeTitle Finished!")
            .setContentText("Otsukaresama! Great work under the stars ✨")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(HoshiyaApplication.TIMER_NOTIFICATION_ID, notification)
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val remaining = _remainingSeconds.value
        val mins = remaining / 60
        val secs = remaining % 60
        val timeString = String.format("%02d:%02d", mins, secs)
        val modeTitle = _currentMode.value.title
        val isRunning = _timerState.value == TimerState.RUNNING

        val pauseResumeIntent = Intent(this, TimerService::class.java).apply {
            action = if (isRunning) ACTION_PAUSE else ACTION_RESUME
        }
        val pauseResumePending = PendingIntent.getService(
            this, 1, pauseResumeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val skipIntent = Intent(this, TimerService::class.java).apply {
            action = ACTION_SKIP
        }
        val skipPending = PendingIntent.getService(
            this, 2, skipIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, HoshiyaApplication.TIMER_CHANNEL_ID)
            .setContentTitle("$timeString • $modeTitle (星夁E")
            .setContentText(if (isRunning) "Focusing calmly under the stars..." else "Paused")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(isRunning)
            .setOnlyAlertOnce(true)
            .addAction(
                if (isRunning) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isRunning) "Pause" else "Resume",
                pauseResumePending
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Skip",
                skipPending
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return builder.build()
    }

    private fun stopServiceInternal() {
        resetTimer()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        wakeLock?.takeIf { it.isHeld }?.release()
    }

    companion object {
        const val ACTION_START = "com.saymaven.hoshiya.action.START"
        const val ACTION_PAUSE = "com.saymaven.hoshiya.action.PAUSE"
        const val ACTION_RESUME = "com.saymaven.hoshiya.action.RESUME"
        const val ACTION_RESET = "com.saymaven.hoshiya.action.RESET"
        const val ACTION_SKIP = "com.saymaven.hoshiya.action.SKIP"
        const val ACTION_STOP_SERVICE = "com.saymaven.hoshiya.action.STOP_SERVICE"
    }
}
