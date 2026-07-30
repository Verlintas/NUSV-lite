package com.nusv.lite.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object HapticPrefs {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("haptic_prefs", Context.MODE_PRIVATE)
    }

    fun isEnabled(): Boolean = prefs?.getBoolean("haptic_enabled", true) ?: true

    fun setEnabled(enabled: Boolean) {
        prefs?.edit()?.putBoolean("haptic_enabled", enabled)?.apply()
    }
}

fun HapticFeedback.performIfEnabled() {
    if (HapticPrefs.isEnabled()) {
        this.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}
