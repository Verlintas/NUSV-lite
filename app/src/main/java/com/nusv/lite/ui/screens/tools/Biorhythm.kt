package com.nusv.lite.ui.screens.tools

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.sin

private fun calculateBiorhythm(birthDate: Calendar, targetDate: Calendar, cycle: Int): Double {
    val diff = (targetDate.timeInMillis - birthDate.timeInMillis) / (1000 * 60 * 60 * 24)
    val days = diff.toInt()
    val radians = 2 * PI * days / cycle
    return sin(radians) * 100
}

@Composable
fun Biorhythm(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    var birthYear by remember { mutableIntStateOf(1990) }
    var birthMonth by remember { mutableIntStateOf(1) }
    var birthDay by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()),
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
            Text("Biorhythm", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        DatePicker("Year", birthYear, { if (birthYear > 1900) birthYear-- }, { if (birthYear < 2100) birthYear++ })
        Spacer(Modifier.height(8.dp))
        DatePicker("Month", birthMonth, { if (birthMonth > 1) birthMonth-- }, { if (birthMonth < 12) birthMonth++ })
        Spacer(Modifier.height(8.dp))
        DatePicker("Day", birthDay, { if (birthDay > 1) birthDay-- }, { if (birthDay < 31) birthDay++ })

        Spacer(Modifier.height(20.dp))

        val birthDate = remember(birthYear, birthMonth, birthDay) {
            Calendar.getInstance().apply {
                set(birthYear, birthMonth - 1, birthDay)
            }
        }

        val scores = remember(birthDate) {
            val now = Calendar.getInstance()
            listOf(
                Triple("Physical", calculateBiorhythm(birthDate, now, 23), Color.Red),
                Triple("Emotional", calculateBiorhythm(birthDate, now, 28), Color.Blue),
                Triple("Intellectual", calculateBiorhythm(birthDate, now, 33), Color.Green),
            )
        }

        val criticalDays = remember(birthDate) {
            val now = Calendar.getInstance()
            mutableListOf<Int>().apply {
                for (day in 0..30) {
                    val cal = now.clone() as Calendar
                    cal.add(Calendar.DAY_OF_YEAR, day)
                    for (cycle in listOf(23, 28, 33)) {
                        val bio = calculateBiorhythm(birthDate, cal, cycle)
                        if (kotlin.math.abs(bio) < 5.0) {
                            add(day)
                            break
                        }
                    }
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxWidth().height(280.dp).padding(8.dp)) {
            val w = size.width
            val h = size.height
            val padding = 40f

            for (i in 0..4) {
                val y = padding + (h - 2 * padding) * (1 - i / 4f)
                drawLine(Color.Gray.copy(alpha = 0.3f), Offset(padding, y), Offset(w - padding, y), strokeWidth = 1f)
            }

            val cycles = listOf(23 to Color.Red, 28 to Color.Blue, 33 to Color.Green)
            for ((cycle, color) in cycles) {
                val path = Path()
                for (day in 0..30) {
                    val x = padding + (w - 2 * padding) * day / 30f
                    val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, day) }
                    val bio = calculateBiorhythm(birthDate, cal, cycle)
                    val y = padding + (h - 2 * padding) * (1f - (bio.toFloat() + 100f) / 200f)
                    if (day == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path, color, style = Stroke(width = 2f))
            }

            val centerX = padding + (w - 2 * padding) * 0f
            drawLine(Color.Gray.copy(alpha = 0.6f), Offset(centerX, padding), Offset(centerX, h - padding), strokeWidth = 1f)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            LegendItem(Color.Red, "Physical (23d)")
            LegendItem(Color.Blue, "Emotional (28d)")
            LegendItem(Color.Green, "Intellectual (33d)")
        }

        Spacer(Modifier.height(16.dp))

        Text("Today's Scores", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        for ((label, score, color) in scores) {
            ScoreRow(label, score, color)
        }

        Spacer(Modifier.height(12.dp))

        if (criticalDays.contains(0)) {
            Text(
                text = "⚠ Critical day today!",
                color = Color(0xFFFF9800),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }

        if (criticalDays.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Critical days: ${criticalDays.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DatePicker(label: String, value: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable { onDecrement() },
                contentAlignment = Alignment.Center,
            ) {
                Text("-", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable { onIncrement() },
                contentAlignment = Alignment.Center,
            ) {
                Text("+", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
    }
}

@Composable
private fun ScoreRow(label: String, score: Double, color: Color) {
    val status = when {
        score > 50 -> "High / Good"
        score < -50 -> "Low / Caution"
        else -> "Average"
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
            Spacer(Modifier.width(6.dp))
            Text("$label: ${"%.1f".format(score)}%", style = MaterialTheme.typography.bodyMedium)
        }
        Text(status, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
}
