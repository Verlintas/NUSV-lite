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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.SoundManager
import com.nusv.lite.util.performIfEnabled
import kotlin.random.Random

private fun formatNum(n: Int): String = if (n < 10) "0$n" else "$n"

private fun lotterySet(): List<List<Int>> {
    val reds = (1..35).shuffled(Random).take(5).sorted()
    val blues = (1..12).shuffled(Random).take(2).sorted()
    return listOf(reds, blues)
}

@Composable
fun LotteryGen(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current

    var sets by remember { mutableIntStateOf(1) }
    var results by remember { mutableStateOf<List<List<List<Int>>>>(emptyList()) }
    var lastMsg by remember { mutableStateOf<String?>(null) }

    fun generate() {
        results = List(sets) { lotterySet() }
        lastMsg = null
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
            Text(strings.toolTitles["lottery"] ?: "Lottery", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(40.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(strings.lotteryCount, style = MaterialTheme.typography.bodyLarge)
            (1..5).forEach { n ->
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            if (sets == n) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            sets = n
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$n",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (sets == n) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Button(
            onClick = { haptic.performIfEnabled(); generate() },
            modifier = Modifier.fillMaxWidth()
        ) { Text(strings.lotteryGenerate) }

        Spacer(Modifier.height(16.dp))

        if (results.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎰", style = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(8.dp))
                Text(strings.lotteryIntro, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text(strings.lotteryRule, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            results.forEachIndexed { i, (reds, blues) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("#${i + 1}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    reds.forEach { n ->
                        Ball(n, Color(0xFFE53935))
                    }
                    Text("+", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    blues.forEach { n ->
                        Ball(n, Color(0xFF1E88E5))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            lastMsg?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(strings.lotteryWarn, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun Ball(n: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(color, RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            formatNum(n),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
