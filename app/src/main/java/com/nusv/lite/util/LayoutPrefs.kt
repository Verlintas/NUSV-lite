package com.nusv.lite.util

import android.content.Context
import android.content.SharedPreferences

enum class LayoutMode(val value: String) {
    LIST("list"),
    GRID_2("grid2"),
    GRID_3("grid3")
}

object LayoutPrefs {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("layout_prefs", Context.MODE_PRIVATE)
    }

    fun get(): LayoutMode {
        val v = prefs?.getString("layout", LayoutMode.LIST.value) ?: LayoutMode.LIST.value
        return LayoutMode.entries.find { it.value == v } ?: LayoutMode.LIST
    }

    fun set(mode: LayoutMode) {
        prefs?.edit()?.putString("layout", mode.value)?.apply()
    }
}
