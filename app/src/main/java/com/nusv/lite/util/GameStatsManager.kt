package com.nusv.lite.util

import android.content.Context

object GameStatsManager {
    private const val PREFS_NAME = "game_stats"
    private const val KEY_PREFIX_HIGH = "high_"
    private const val KEY_PREFIX_WIN = "stat_w_"
    private const val KEY_PREFIX_LOSS = "stat_l_"
    private const val KEY_PREFIX_DRAW = "stat_d_"

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

    data class GameRecord(val wins: Int, val losses: Int, val draws: Int) {
        val total: Int get() = wins + losses + draws
        val winRate: Int get() = if (total == 0) 0 else ((wins * 100) / total.toFloat()).toInt()
    }

    fun getRecord(c: Context, gameId: String): GameRecord =
        GameRecord(
            wins = prefs(c).getInt(KEY_PREFIX_WIN + gameId, 0),
            losses = prefs(c).getInt(KEY_PREFIX_LOSS + gameId, 0),
            draws = prefs(c).getInt(KEY_PREFIX_DRAW + gameId, 0),
        )

    fun recordResult(c: Context, gameId: String, result: String) {
        val key = when (result) {
            "win" -> KEY_PREFIX_WIN
            "loss" -> KEY_PREFIX_LOSS
            else -> KEY_PREFIX_DRAW
        } + gameId
        prefs(c).edit().putInt(key, prefs(c).getInt(key, 0) + 1).apply()
    }
}
