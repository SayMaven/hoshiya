package com.saymaven.hoshiya.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.saymaven.hoshiya.core.model.AmbientSound

/**
 * High-Reliability Audio Engine for Hoshiya.
 * Plays pristine looping ambient audio samples using hardware-accelerated Android MediaPlayer.
 */
class AmbientEngine(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    @Volatile
    private var currentVolume: Float = 0.5f

    @Volatile
    private var currentSound: AmbientSound = AmbientSound.OFF

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        try {
            mediaPlayer?.setVolume(currentVolume, currentVolume)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play(sound: AmbientSound, volume: Float = 0.5f) {
        currentVolume = volume
        if (sound == currentSound && isPlaying()) {
            setVolume(volume)
            return
        }

        stop()
        currentSound = sound

        if (sound == AmbientSound.OFF || sound.resId == null) {
            return
        }

        try {
            val player = MediaPlayer.create(context, sound.resId).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                setVolume(currentVolume, currentVolume)
                start()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun stop() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            currentSound = AmbientSound.OFF
        }
    }
}
