package com.hoshiya.app.core.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.hoshiya.app.core.model.AmbientSound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.sin

class AmbientEngine {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var playbackJob: Job? = null
    private var audioTrack: AudioTrack? = null
    private val random = Random()

    @Volatile
    private var currentVolume: Float = 0.5f

    @Volatile
    private var currentSound: AmbientSound = AmbientSound.OFF

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        try {
            audioTrack?.setVolume(currentVolume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play(sound: AmbientSound, volume: Float = 0.5f) {
        if (sound == currentSound && isPlaying()) return
        stop()

        currentSound = sound
        currentVolume = volume

        if (sound == AmbientSound.OFF) return

        playbackJob = scope.launch {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            try {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.setVolume(currentVolume)
                track.play()

                val buffer = ShortArray(bufferSize)
                var phase = 0.0
                var brownNoise = 0.0

                while (isActive) {
                    for (i in buffer.indices) {
                        var sample = 0.0

                        when (currentSound) {
                            AmbientSound.STARRY_NIGHT -> {
                                // Celestial sine drone at 110Hz + 220Hz harmonic with slow modulation
                                phase += 2.0 * PI * 110.0 / sampleRate
                                val mod = sin(phase * 0.05) * 0.5 + 0.5
                                sample = sin(phase) * 0.25 + sin(phase * 2.0) * 0.15 * mod
                                val white = (random.nextDouble() * 2.0 - 1.0) * 0.02
                                sample += white
                            }
                            AmbientSound.LOFI_RAIN -> {
                                // Low-pass filtered pink/brown noise
                                val white = random.nextDouble() * 2.0 - 1.0
                                brownNoise = (brownNoise + (0.04 * white)) / 1.04
                                sample = brownNoise * 3.5
                            }
                            AmbientSound.COZY_ROOM -> {
                                // Warm low drone with gentle vinyl-like crackles
                                val white = random.nextDouble() * 2.0 - 1.0
                                brownNoise = (brownNoise + (0.02 * white)) / 1.02
                                sample = brownNoise * 2.0
                                if (random.nextInt(400) == 0) {
                                    sample += (random.nextDouble() - 0.5) * 0.8
                                }
                            }
                            AmbientSound.MIDNIGHT_CAFE -> {
                                // Mellow low acoustic presence
                                val white = random.nextDouble() * 2.0 - 1.0
                                brownNoise = (brownNoise + (0.03 * white)) / 1.03
                                phase += 2.0 * PI * 130.8 / sampleRate
                                sample = brownNoise * 2.5 + sin(phase) * 0.1
                            }
                            AmbientSound.OFF -> {
                                sample = 0.0
                            }
                        }

                        val intSample = (sample * Short.MAX_VALUE * 0.5).toInt().coerceIn(
                            Short.MIN_VALUE.toInt(),
                            Short.MAX_VALUE.toInt()
                        )
                        buffer[i] = intSample.toShort()
                    }

                    track.write(buffer, 0, buffer.size)
                }

                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isPlaying(): Boolean = playbackJob?.isActive == true

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
        currentSound = AmbientSound.OFF
    }
}
