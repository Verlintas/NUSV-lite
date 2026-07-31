package com.nusv.lite.util

import android.content.Context

data class Achievement(
    val id: String,
    val icon: String,
    val zhName: String,
    val enName: String,
    val zhDesc: String,
    val enDesc: String,
)

object AchievementManager {
    private const val PREFS_NAME = "achievements"
    private const val KEY_UNLOCKED = "unlocked"
    private const val KEY_GAMES = "games_played"
    private const val KEY_TOOLS = "tools_used"

    val ALL = listOf(
        Achievement("first_game", "🎮", "初次游戏", "First Game", "游玩任意游戏 1 次", "Play any game once"),
        Achievement("game_fan", "🕹️", "游戏爱好者", "Game Fan", "累计游玩游戏 10 次", "Play games 10 times in total"),
        Achievement("game_master", "👑", "游戏大师", "Game Master", "累计游玩游戏 50 次", "Play games 50 times in total"),
        Achievement("tool_explorer", "🧭", "工具探索者", "Tool Explorer", "使用过 10 种不同工具", "Use 10 different tools"),
        Achievement("tool_collector", "🧰", "工具收藏家", "Tool Collector", "使用过 25 种不同工具", "Use 25 different tools"),
        Achievement("streak7", "🔥", "七日之约", "7-Day Streak", "连续签到 7 天", "Check in 7 days in a row"),
        Achievement("streak30", "⚡", "月度坚持", "Monthly Dedication", "连续签到 30 天", "Check in 30 days in a row"),
        Achievement("points5000", "💰", "小有积蓄", "First Savings", "积分余额达到 5,000", "Reach 5,000 points"),
        Achievement("points20000", "💎", "财富自由", "Well Off", "积分余额达到 20,000", "Reach 20,000 points"),
        Achievement("orca_owner", "🌊", "ORCA", "ORCA", "拥有 Orca 高级主题", "Own the Orca premium theme"),
    )

    private fun prefs(c: Context) = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun unlockedSet(c: Context): Set<String> =
        prefs(c).getStringSet(KEY_UNLOCKED, emptySet()) ?: emptySet()

    fun isUnlocked(c: Context, id: String): Boolean = id in unlockedSet(c)

    fun unlockedCount(c: Context): Int = unlockedSet(c).size

    fun getGamesPlayed(c: Context): Int = prefs(c).getInt(KEY_GAMES, 0)

    fun getToolsUsed(c: Context): Int = toolsSet(c).size

    private fun toolsSet(c: Context): Set<String> =
        prefs(c).getStringSet(KEY_TOOLS, emptySet()) ?: emptySet()

    private fun unlock(c: Context, id: String) {
        val set = unlockedSet(c).toMutableSet()
        if (set.add(id)) {
            prefs(c).edit().putStringSet(KEY_UNLOCKED, set).apply()
        }
    }

    fun recordUse(c: Context, toolId: String, isGame: Boolean) {
        val editor = prefs(c).edit()
        if (isGame) {
            editor.putInt(KEY_GAMES, getGamesPlayed(c) + 1)
        } else {
            val tools = toolsSet(c).toMutableSet()
            tools.add(toolId)
            editor.putStringSet(KEY_TOOLS, tools)
        }
        editor.apply()
        checkAndUnlock(c)
    }

    fun checkAndUnlock(c: Context) {
        val games = getGamesPlayed(c)
        val tools = getToolsUsed(c)
        val streak = PointsManager.getStreak(c)
        val balance = PointsManager.getBalance(c)

        if (games >= 1) unlock(c, "first_game")
        if (games >= 10) unlock(c, "game_fan")
        if (games >= 50) unlock(c, "game_master")
        if (tools >= 10) unlock(c, "tool_explorer")
        if (tools >= 25) unlock(c, "tool_collector")
        if (streak >= 7) unlock(c, "streak7")
        if (streak >= 30) unlock(c, "streak30")
        if (balance >= 5000) unlock(c, "points5000")
        if (balance >= 20000) unlock(c, "points20000")
        if (PointsManager.isOrcaPurchased(c)) unlock(c, "orca_owner")
    }
}
