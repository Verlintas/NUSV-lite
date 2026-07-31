package com.nusv.lite.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nusv.lite.util.Lang
import com.nusv.lite.util.performIfEnabled
import com.nusv.lite.util.scalePress
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nusv.lite.data.SyncManager
import com.nusv.lite.repository.AppRepository
import com.nusv.lite.ui.screens.DetailScreen
import com.nusv.lite.ui.screens.DiscoverScreen
import com.nusv.lite.ui.screens.DocDetailScreen
import com.nusv.lite.ui.screens.DocsScreen
import com.nusv.lite.ui.screens.HomeScreen
import com.nusv.lite.ui.screens.SearchScreen
import com.nusv.lite.ui.screens.SettingsScreen

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    data object Docs : Screen("docs", "Docs", Icons.Outlined.Description, Icons.Filled.Description)
    data object Discover : Screen("discover", "Discover", Icons.Outlined.Widgets, Icons.Filled.Widgets)
    data object Settings : Screen("settings", "Settings", Icons.Outlined.Settings, Icons.Filled.Settings)
}

private val bottomNavItems = listOf(Screen.Home, Screen.Docs, Screen.Discover, Screen.Settings)

@Composable
fun NusvBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scheme = MaterialTheme.colorScheme
    val isDark = scheme.background == Color(0xFF000000)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp, vertical = 16.dp)
            .height(56.dp)
            .background(
                if (isDark) Color(0xCC1A1A1A) else Color(0xCCFFFFFF),
                RoundedCornerShape(28.dp)
            )
            .border(
                0.5.dp,
                if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f),
                RoundedCornerShape(28.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { screen ->
                val selected = currentRoute == screen.route
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .scalePress(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { haptic.performIfEnabled(); onNavigate(screen) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selected) screen.selectedIcon else screen.icon,
                        contentDescription = screen.label,
                        tint = if (selected) scheme.primary else scheme.onSurfaceVariant,
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NusvNavHost(
    repository: AppRepository,
    syncManager: SyncManager,
    onThemeChange: ((Boolean?) -> Unit)? = null,
    onLanguageChange: ((Lang) -> Unit)? = null,
    onThemeSelected: ((String) -> Unit)? = null,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        val toolId = (context as? androidx.activity.ComponentActivity)?.intent?.getStringExtra("tool_id")
        if (toolId != null) {
            com.nusv.lite.ui.screens._pendingToolId = toolId
        }
    }

    val topLevelRoutes = bottomNavItems.map { it.route }
    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)) },
                exitTransition = { fadeOut(tween(200)) },
                popEnterTransition = { fadeIn(tween(200)) },
                popExitTransition = { slideOutHorizontally(tween(300)) { it / 4 } + fadeOut(tween(200)) }
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        repository = repository,
                        syncManager = syncManager,
                        onItemClick = { itemId ->
                            navController.navigate("detail/$itemId")
                        },
                        onSearchClick = {
                            navController.navigate("search")
                        },
                        onToolClick = {
                            navController.navigate(Screen.Discover.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Screen.Docs.route) {
                    DocsScreen(
                        repository = repository,
                        onDocClick = { docId ->
                            navController.navigate("doc/$docId")
                        }
                    )
                }
                composable(Screen.Discover.route) {
                    DiscoverScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        syncManager = syncManager,
                        onThemeChange = onThemeChange,
                        onLanguageChange = onLanguageChange,
                        onThemeShopClick = { navController.navigate("themes") },
                        onAchievementsClick = { navController.navigate("achievements") },
                    )
                }
                composable("achievements") {
                    com.nusv.lite.ui.screens.AchievementsScreen(
                        onBack = { navController.popBackStack() },
                    )
                }
                composable("themes") {
                    com.nusv.lite.ui.screens.ThemeShopScreen(
                        onBack = { navController.popBackStack() },
                        onThemeSelected = { theme -> onThemeSelected?.invoke(theme) },
                    )
                }
                composable("detail/{itemId}") { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                    DetailScreen(
                        itemId = itemId,
                        repository = repository,
                        syncManager = syncManager,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("doc/{docId}") { backStackEntry ->
                    val docId = backStackEntry.arguments?.getString("docId") ?: ""
                    DocDetailScreen(
                        docId = docId,
                        repository = repository,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("search") {
                    SearchScreen(
                        repository = repository,
                        onItemClick = { itemId ->
                            navController.navigate("detail/$itemId")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            if (showBottomBar) {
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    NusvBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
