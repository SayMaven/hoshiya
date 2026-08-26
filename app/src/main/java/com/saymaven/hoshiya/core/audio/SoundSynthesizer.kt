package com.saymaven.hoshiya.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object SoundSynthesizer {

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays a celestial Japanese crystal bell chime chord (E5, G#5, B5, E6 harmonics).
     */
    fun playCompletionChime() {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSeconds = 1.8
                val numSamples = (durationSeconds * sampleRate).toInt()
                val buffer = ShortArray(numSamples)

                val freqs = doubleArrayOf(659.25, 830.61, 987.77, 1318.51) // E5 major chord
                val weights = doubleArrayOf(0.4, 0.3, 0.2, 0.15)
                val decays = doubleArrayOf(2.5, 3.0, 3.5, 4.0)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    var sampleVal = 0.0
                    for (k in freqs.indices) {
                        val envelope = exp(-decays[k] * t)
                        sampleVal += weights[k] * sin(2.0 * PI * freqs[k] * t) * envelope
                    }
                    val clamped = (sampleVal * Short.MAX_VALUE * 0.8).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    buffer[i] = clamped.toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                
                // Allow audio to play out then release
                Thread.sleep((durationSeconds * 1000).toLong())
                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Plays a soft button/mode switch tick.
     */
    fun playClickSound() {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSeconds = 0.04
                val numSamples = (durationSeconds * sampleRate).toInt()
                val buffer = ShortArray(numSamples)
                val freq = 880.0

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = exp(-80.0 * t)
                    val sampleVal = sin(2.0 * PI * freq * t) * envelope * 0.3
                    buffer[i] = (sampleVal * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                Thread.sleep(60)
                track.stop()
                track.release()
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
