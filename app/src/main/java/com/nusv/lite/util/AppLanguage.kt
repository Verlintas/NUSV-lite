package com.nusv.lite.util

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

enum class Lang(val code: String, val label: String, val locale: Locale) {
    ZH("zh", "中文", Locale.CHINESE),
    EN("en", "English", Locale.ENGLISH)
}

object LanguagePrefs {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    }

    fun get(): Lang {
        val code = prefs?.getString("lang", Lang.ZH.code) ?: Lang.ZH.code
        return Lang.entries.find { it.code == code } ?: Lang.ZH
    }

    fun set(lang: Lang) {
        prefs?.edit()?.putString("lang", lang.code)?.apply()
    }
}
