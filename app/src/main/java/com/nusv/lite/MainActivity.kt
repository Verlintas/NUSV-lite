package com.nusv.lite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.nusv.lite.data.AppDatabase
import com.nusv.lite.data.ContentLoader
import com.nusv.lite.data.SyncManager
import com.nusv.lite.repository.AppRepository
import com.nusv.lite.ui.navigation.NusvNavHost
import com.nusv.lite.ui.theme.NusvTheme
import com.nusv.lite.util.ENStrings
import com.nusv.lite.util.HapticPrefs
import com.nusv.lite.util.LanguagePrefs
import com.nusv.lite.util.LayoutPrefs
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.ZHStrings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "nusv-lite.db"
        ).fallbackToDestructiveMigration(true).build()

        HapticPrefs.init(applicationContext)
        LanguagePrefs.init(applicationContext)
        LayoutPrefs.init(applicationContext)
        val loader = ContentLoader(applicationContext)
        val syncManager = SyncManager(applicationContext, db)

        lifecycleScope.launch(Dispatchers.IO) {
            loader.loadIfNeeded(db)
            syncManager.syncAll()
        }

        val repository = AppRepository(db)

        setContent {
            var isDark by remember { mutableStateOf<Boolean?>(null) }
            var currentLang by remember { mutableStateOf(LanguagePrefs.get()) }
            var currentTheme by remember { mutableStateOf(PointsManager.getSelectedTheme(applicationContext)) }
            val orcaForcedDark = currentTheme == PointsManager.ORCA_THEME && PointsManager.isOrcaPurchased(applicationContext)
            val appStrings = if (currentLang.code == "en") ENStrings else ZHStrings

            CompositionLocalProvider(LocalAppStrings provides appStrings) {
                NusvTheme(
                    darkTheme = if (orcaForcedDark) true
                               else when (isDark) {
                        true -> true
                        false -> false
                        null -> isSystemInDarkTheme()
                    },
                    themeName = currentTheme,
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        NusvNavHost(
                            repository = repository,
                            syncManager = syncManager,
                            onThemeChange = { isDark = it },
                            onLanguageChange = { lang ->
                                LanguagePrefs.set(lang)
                                currentLang = lang
                            },
                            onThemeSelected = { theme ->
                                PointsManager.setSelectedTheme(applicationContext, theme)
                                currentTheme = theme
                            },
                        )
                    }
                }
            }
        }
    }
}
