package com.saymaven.hoshiya.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.saymaven.hoshiya.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SoundSynthesizer {

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays the pristine crystal Japanese bell chime sample from res/raw.
     */
    fun playCompletionChime(context: Context? = null) {
        scope.launch {
            try {
                if (context != null) {
                    val player = MediaPlayer.create(context, R.raw.bell_chime).apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                        )
                        setOnCompletionListener { mp ->
                            mp.release()
                        }
                        start()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Triggers a gentle double-pulse haptic vibration.
     */
    fun vibrateGentle(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 120, 80, 160)
                val amplitudes = intArrayOf(0, 180, 0, 220)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 150, 100, 200), -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
