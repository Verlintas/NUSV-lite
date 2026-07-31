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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.SoundManager
import com.nusv.lite.util.performIfEnabled

private fun formatTime(h: Int, m: Int): String {
    val hh = if (h % 24 < 10) "0${h % 24}" else "${h % 24}"
    val mm = if (m < 10) "0$m" else "$m"
    return "$hh:$mm"
}

@Composable
fun SleepCalc(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current

    var wakeH by remember { mutableStateOf(7) }
    var wakeM by remember { mutableStateOf(0) }
    var wakeHText by remember { mutableStateOf("7") }
    var wakeMText by remember { mutableStateOf("0") }
    var bedH by remember { mutableStateOf(23) }
    var bedM by remember { mutableStateOf(0) }
    var bedHText by remember { mutableStateOf("23") }
    var bedMText by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf<String?>(null) }

    fun computeWake() {
        val h = wakeHText.toIntOrNull() ?: return
        val m = wakeMText.toIntOrNull() ?: return
        if (h !in 0..23 || m !in 0..59) return
        wakeH = h
        wakeM = m
        val wakeMinutes = h * 60 + m
        val sb = StringBuilder()
        for (cycle in 5 downTo 3) {
            val total = cycle * 90
            val minutes = (wakeMinutes - total + 1440) % 1440
            val bh = minutes / 60
            val bm = minutes % 60
            sb.appendLine(strings.sleepCycle.format(cycle) + ": " + formatTime(bh, bm))
        }
        result = sb.toString()
        SoundManager.playSuccess()
    }

    fun computeBed() {
        val h = bedHText.toIntOrNull() ?: return
        val m = bedMText.toIntOrNull() ?: return
        if (h !in 0..23 || m !in 0..59) return
        bedH = h
        bedM = m
        val bedMinutes = h * 60 + m
        val sb = StringBuilder()
        for (cycle in 5 downTo 3) {
            val total = cycle * 90
            val minutes = (bedMinutes + total) % 1440
            val wh = minutes / 60
            val wm = minutes % 60
            sb.appendLine(strings.sleepCycle.format(cycle) + ": " + formatTime(wh, wm))
        }
        result = sb.toString()
        SoundManager.playSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBack() }) { Text("‹") }
            Text(strings.toolTitles["sleepcalc"] ?: "Sleep Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(40.dp))
        }

        Text(strings.sleepWakeIntro, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(12.dp))

        Text(strings.sleepWakeTime, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = wakeHText,
                onValueChange = { wakeHText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("HH") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(90.dp)
            )
            Text(":", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = wakeMText,
                onValueChange = { wakeMText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("MM") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(90.dp)
            )
            Spacer(Modifier.width(4.dp))
            Button(onClick = { haptic.performIfEnabled(); computeWake() }) { Text(strings.sleepGoToBed) }
        }

        Spacer(Modifier.height(20.dp))

        Text(strings.sleepBedTime, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = bedHText,
                onValueChange = { bedHText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("HH") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(90.dp)
            )
            Text(":", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = bedMText,
                onValueChange = { bedMText = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("MM") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(90.dp)
            )
            Spacer(Modifier.width(4.dp))
            Button(onClick = { haptic.performIfEnabled(); computeBed() }) { Text(strings.sleepWakeUp) }
        }

        Spacer(Modifier.height(20.dp))

        result?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(strings.sleepResult, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(strings.sleepHint, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }
}
