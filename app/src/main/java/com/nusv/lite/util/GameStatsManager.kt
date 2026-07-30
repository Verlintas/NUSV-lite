package com.nusv.lite.util

import android.content.Context

object GameStatsManager {
    private const val PREFS_NAME = "game_stats"
    private const val KEY_PREFIX_HIGH = "high_"

    private fun prefs(c: Context) = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getHighScore(c: Context, gameId: String): Int =
        prefs(c).getInt(KEY_PREFIX_HIGH + gameId, 0)

    fun setHighScore(c: Context, gameId: String, score: Int) {
        val key = KEY_PREFIX_HIGH + gameId
        val current = prefs(c).getInt(key, 0)
        if (score > current) {
            prefs(c).edit().putInt(key, score).apply()
        }
    }

    fun isNewHighScore(c: Context, gameId: String, score: Int): Boolean =
        score > prefs(c).getInt(KEY_PREFIX_HIGH + gameId, 0)
}
