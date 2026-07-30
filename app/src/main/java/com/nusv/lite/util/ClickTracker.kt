package com.nusv.lite.util

import android.content.Context

object ClickTracker {
    private const val PREFS_NAME = "click_tracker"
    private const val RECENT_KEY = "recent_list"
    private const val MAX_RECENT = 5

    fun getCounts(context: Context): Map<String, Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val result = mutableMapOf<String, Int>()
        for ((key, value) in prefs.all) {
            if (!key.startsWith("_") && value is Int) {
                result[key] = value
            }
        }
        return result
    }

    fun getRecent(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(RECENT_KEY, emptySet())?.toList()
            ?.sortedByDescending { prefs.getLong("_ts_$it", 0L) }
            ?: emptyList()
    }

    fun increment(context: Context, toolId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = prefs.getInt(toolId, 0)
        prefs.edit()
            .putInt(toolId, current + 1)
            .putLong("_ts_$toolId", System.currentTimeMillis())
            .apply()
        updateRecentList(context, toolId)
    }

    private fun updateRecentList(context: Context, toolId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val recent = prefs.getStringSet(RECENT_KEY, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        recent.add(toolId)
        if (recent.size > MAX_RECENT) {
            val sorted = recent.sortedByDescending { prefs.getLong("_ts_$it", 0L) }
            val trimmed = sorted.take(MAX_RECENT).toSet()
            prefs.edit().putStringSet(RECENT_KEY, trimmed).apply()
        } else {
            prefs.edit().putStringSet(RECENT_KEY, recent).apply()
        }
    }
}
