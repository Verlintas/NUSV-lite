package com.nusv.lite.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator

object SoundPrefs {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
    }

    fun isEnabled(): Boolean = prefs?.getBoolean("sound_enabled", true) ?: true

    fun setEnabled(enabled: Boolean) {
        prefs?.edit()?.putBoolean("sound_enabled", enabled)?.apply()
    }
}

object SoundManager {
    enum class Sound {
        TAP, SUCCESS, ERROR, WIN
    }

    @Volatile
    private var toneGen: ToneGenerator? = null

    private fun ensureGen(): ToneGenerator? {
        if (toneGen == null) {
            synchronized(this) {
                if (toneGen == null) {
                    toneGen = try {
                        ToneGenerator(AudioManager.STREAM_MUSIC, 60)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
        }
        return toneGen
    }

    fun play(sound: Sound) {
        if (!SoundPrefs.isEnabled()) return
        val gen = ensureGen() ?: return
        val tone = when (sound) {
            Sound.TAP -> ToneGenerator.TONE_PROP_BEEP
            Sound.SUCCESS -> ToneGenerator.TONE_PROP_ACK
            Sound.ERROR -> ToneGenerator.TONE_PROP_NACK
            Sound.WIN -> ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
        }
        try {
            gen.startTone(tone, 80)
        } catch (_: Exception) {
        }
    }

    fun playTap() = play(Sound.TAP)
    fun playSuccess() = play(Sound.SUCCESS)
    fun playError() = play(Sound.ERROR)
    fun playWin() = play(Sound.WIN)
}
