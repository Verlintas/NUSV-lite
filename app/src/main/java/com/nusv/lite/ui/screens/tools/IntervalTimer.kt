package com.nusv.lite.ui.screens.tools

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay

@Composable
fun IntervalTimer(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    var workSeconds by remember { mutableIntStateOf(30) }
    var restSeconds by remember { mutableIntStateOf(10) }
    var rounds by remember { mutableIntStateOf(5) }
    var currentRound by remember { mutableIntStateOf(1) }
    var isWork by remember { mutableStateOf(true) }
    var remaining by remember { mutableIntStateOf(30) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (running) {
                remaining--
                if (remaining <= 0) {
                    if (isWork) {
                        isWork = false
                        remaining = restSeconds
                        haptic.performIfEnabled()
                    } else if (currentRound < rounds) {
                        currentRound++
                        isWork = true
                        remaining = workSeconds
                        haptic.performIfEnabled()
                    } else {
                        running = false
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Text("Interval Timer", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(24.dp))

        TimerSetting("Work", workSeconds, { if (workSeconds > 1) workSeconds-- }, { if (workSeconds < 300) workSeconds++ }, !running)
        Spacer(Modifier.height(12.dp))
        TimerSetting("Rest", restSeconds, { if (restSeconds > 1) restSeconds-- }, { if (restSeconds < 300) restSeconds++ }, !running)
        Spacer(Modifier.height(12.dp))
        TimerSetting("Rounds", rounds, { if (rounds > 1) rounds-- }, { if (rounds < 50) rounds++ }, !running)

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(220.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isWork) "WORK" else "REST",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isWork) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = remaining.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Round $currentRound/$rounds",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TimerButton(
                text = if (running) "Stop" else "Start",
                onClick = {
                    haptic.performIfEnabled()
                    if (running) {
                        running = false
                    } else {
                        if (remaining <= 0 || (isWork && remaining > workSeconds) || (!isWork && remaining > restSeconds)) {
                            isWork = true
                            currentRound = 1
                            remaining = workSeconds
                        }
                        running = true
                    }
                },
            )
            if (!running) {
                TimerButton(
                    text = "Reset",
                    onClick = {
                        haptic.performIfEnabled()
                        running = false
                        isWork = true
                        currentRound = 1
                        remaining = workSeconds
                    },
                )
            }
        }
    }
}

@Composable
private fun TimerSetting(label: String, value: Int, onDecrement: () -> Unit, onIncrement: () -> Unit, enabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(if (enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant).clickable(enabled) { onDecrement() },
                contentAlignment = Alignment.Center,
            ) {
                Text("-", style = MaterialTheme.typography.titleMedium, color = if (enabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(if (enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant).clickable(enabled) { onIncrement() },
                contentAlignment = Alignment.Center,
            ) {
                Text("+", style = MaterialTheme.typography.titleMedium, color = if (enabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun TimerButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primary).clickable { onClick() }.padding(horizontal = 32.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
    }
}
