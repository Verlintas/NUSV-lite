package com.nusv.lite.ui.screens.tools

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay

@Composable
fun Metronome(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var bpm by remember { mutableIntStateOf(120) }
    var running by remember { mutableStateOf(false) }
    var timeSig by remember { mutableStateOf("4/4") }
    var currentBeat by remember { mutableIntStateOf(0) }
    var beatFlash by remember { mutableStateOf(false) }
    var isAccent by remember { mutableStateOf(false) }
    val tapTimes = remember { mutableListOf<Long>() }

    val beatsPerMeasure = when (timeSig) {
        "2/4" -> 2
        "3/4" -> 3
        "4/4" -> 4
        "6/8" -> 6
        else -> 4
    }

    val scale by animateFloatAsState(
        targetValue = if (beatFlash) 1.15f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "beatScale",
    )

    val timeSigs = listOf("2/4", "3/4", "4/4", "6/8")

    LaunchedEffect(running, bpm, timeSig) {
        if (running) {
            val intervalMs = 60000L / bpm
            var beat = 0
            while (true) {
                isAccent = (beat % beatsPerMeasure == 0)
                currentBeat = (beat % beatsPerMeasure) + 1
                beatFlash = true
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(50)
                }
                delay(50)
                beatFlash = false
                delay(intervalMs - 50)
                beat++
            }
        }
    }

    val beatCircleColor = if (beatFlash) {
        if (isAccent) Color(0xFF4CAF50) else Color(0xFF81C784)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
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
            Text("Metronome", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = bpm.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BpmButton("-5", { if (bpm > 20) bpm = (bpm - 5).coerceAtLeast(20) }, haptic)
            Spacer(Modifier.width(8.dp))
            BpmButton("-1", { if (bpm > 20) bpm-- }, haptic)
            Spacer(Modifier.width(8.dp))
            BpmButton("+1", { if (bpm < 240) bpm++ }, haptic)
            Spacer(Modifier.width(8.dp))
            BpmButton("+5", { if (bpm < 240) bpm = (bpm + 5).coerceAtMost(240) }, haptic)
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(200.dp)
                .clip(CircleShape)
                .background(beatCircleColor)
                .scale(scale),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (running) currentBeat.toString() else bpm.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold, fontSize = 48.sp),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (running) "Beat $currentBeat / $beatsPerMeasure" else "BPM",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            timeSigs.forEach { sig ->
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (timeSig == sig) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { haptic.performIfEnabled(); timeSig = sig }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        sig,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (timeSig == sig) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    haptic.performIfEnabled()
                    tapTimes.add(System.currentTimeMillis())
                    if (tapTimes.size > 5) tapTimes.removeAt(0)
                    if (tapTimes.size >= 2) {
                        val avg = tapTimes.zipWithNext { a, b -> b - a }.average()
                        if (avg > 0) bpm = (60000 / avg).toInt().coerceIn(20, 240)
                    }
                }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Tap Tempo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    haptic.performIfEnabled()
                    running = !running
                    if (!running) {
                        currentBeat = 0
                        beatFlash = false
                    }
                }
                .padding(horizontal = 48.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (running) "Stop" else "Start",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun BpmButton(text: String, onClick: () -> Unit, haptic: HapticFeedback) {
    Box(
        modifier = Modifier.size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { haptic.performIfEnabled(); onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
