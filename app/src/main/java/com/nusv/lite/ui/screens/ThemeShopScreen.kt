package com.nusv.lite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import com.nusv.lite.ui.theme.availableThemes
import com.nusv.lite.util.AppStrings
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled

@Composable
fun ThemeShopScreen(
    onBack: () -> Unit,
    onThemeSelected: (String) -> Unit,
) {
    val ctx = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    var balance by remember { mutableStateOf(PointsManager.getBalance(ctx)) }
    var checkInMsg by remember { mutableStateOf<String?>(null) }
    var selectedTheme by remember { mutableStateOf(PointsManager.getSelectedTheme(ctx)) }
    val unlocked = remember { mutableStateOf(PointsManager.getUnlocked(ctx)) }
    val applyTheme: (String) -> Unit = { name ->
        PointsManager.setSelectedTheme(ctx, name)
        selectedTheme = name
        onThemeSelected(name)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))                .clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(8.dp))
            Text(strings.themeShopTitle, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("\uD83C\uDF1F", style = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(8.dp))
                Text(strings.themeShopPoints.format(balance), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                val streak = PointsManager.getStreak(ctx)
                if (streak > 0) {
                    Text(strings.shopStreak.format(streak), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
                if (streak >= 7) {
                    Text(strings.shopStreakBonus, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
                Spacer(Modifier.height(8.dp))
                if (checkInMsg != null) {
                    Text(checkInMsg!!, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                }
                Button(
                    onClick = {
                        haptic.performIfEnabled()
                        val earned = PointsManager.checkIn(ctx)
                        if (earned > 0) {
                            checkInMsg = strings.themeShopCheckedIn.format(earned)
                            balance = PointsManager.getBalance(ctx)
                        } else {
                            checkInMsg = strings.themeShopAlready
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(strings.themeShopCheckIn, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(strings.themeShopAvailable, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        availableThemes.forEach { theme ->
            if (theme.name == PointsManager.ORCA_THEME) return@forEach
            val isUnlocked = PointsManager.isUnlocked(ctx, theme.name)
            val isActive = selectedTheme == theme.name
            val price = if (theme.name == "Default (Pink)") 0 else 10

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable {
                        if (isUnlocked) {
                            haptic.performIfEnabled()
                            applyTheme(theme.name)
                        }
                    }
                    .padding(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(theme.lightPrimary),
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(theme.name, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                when {
                                    isActive -> strings.themeShopActive
                                    isUnlocked -> strings.themeShopTapApply
                                    else -> strings.themeShopPointsPrice.format(price)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = when {
                                    isActive -> MaterialTheme.colorScheme.primary
                                    isUnlocked -> MaterialTheme.colorScheme.onSurfaceVariant
                                    else -> MaterialTheme.colorScheme.error
                                },
                            )
                        }
                    }
                    if (isActive) {
                        Text("\u2713", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    } else if (!isUnlocked) {
                        Button(
                            onClick = {
                                haptic.performIfEnabled()
                                if (PointsManager.purchaseTheme(ctx, theme.name)) {
                                    unlocked.value = PointsManager.getUnlocked(ctx)
                                    balance = PointsManager.getBalance(ctx)
                                    applyTheme(theme.name)
                                }
                            },
                            enabled = balance >= price,
                            contentPadding = ButtonDefaults.TextButtonContentPadding,
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(strings.themeShopBuy, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        OrcaSection(ctx, strings, balance, selectedTheme, haptic, applyTheme)

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun OrcaSection(
    ctx: Context,
    strings: AppStrings,
    balance: Int,
    selectedTheme: String,
    haptic: androidx.compose.ui.hapticfeedback.HapticFeedback,
    applyTheme: (String) -> Unit,
) {
    val orcaPurchased = PointsManager.isOrcaPurchased(ctx)
    val orcaActive = selectedTheme == PointsManager.ORCA_THEME
    val eligible = PointsManager.isOrcaEligible(ctx)
    val streak = PointsManager.getStreak(ctx)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (orcaActive) Color.White.copy(alpha = 0.12f) else Color.Black)
            .border(1.dp, Color.White.copy(alpha = if (orcaActive) 0.9f else 0.4f), RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(4.dp))
            Text(PointsManager.ORCA_THEME, style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (orcaActive) "\u2605 Premium Theme Active \u2605"
                       else strings.orcaDesc,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(10.dp))

            // Benefits list
            Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                listOf(
                    strings.orcaBenefitPoints,
                    strings.orcaBenefitPure,
                    strings.orcaBenefitTools,
                    strings.orcaBenefitDark,
                ).forEach { benefit ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(benefit, style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            when {
                !eligible -> {
                    Text(strings.orcaLocked, style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF6B6B))
                    Text("${strings.shopStreak.format(streak)} / 7",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f))
                }
                !orcaPurchased -> {
                    Text(strings.orcaCost, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            haptic.performIfEnabled()
                            if (PointsManager.purchaseOrcaTheme(ctx)) {
                                applyTheme(PointsManager.ORCA_THEME)
                            }
                        },
                        enabled = balance >= 10000,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) { Text(strings.orcaPurchase, fontWeight = FontWeight.Bold) }
                    if (balance < 10000) {
                        Text("${strings.themeShopPoints.format(balance)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f))
                    }
                }
                !orcaActive -> {
                    Button(
                        onClick = {
                            haptic.performIfEnabled()
                            applyTheme(PointsManager.ORCA_THEME)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) { Text("\u25B6 ${strings.orcaTitle}", fontWeight = FontWeight.Bold) }
                }
                else -> {
                    Text(strings.orcaActive, style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Button(
                        onClick = {
                            haptic.performIfEnabled()
                            applyTheme(PointsManager.FREE_THEME)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) { Text(strings.orcaDeactivate, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
