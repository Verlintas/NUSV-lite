package com.nusv.lite.ui.screens.minigames

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.nusv.lite.util.GameStatsManager
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay

data class Card(val id: Int, val symbol: String, val isFlipped: Boolean, val isMatched: Boolean)

@Composable
fun MemoryMatch(onBack: () -> Unit) {
    val symbols = listOf("A", "B", "C", "D", "E", "F", "G", "H")
    val cards = remember { mutableStateOf((symbols + symbols).shuffled().mapIndexed { i, s -> Card(i, s, false, false) }) }
    val flippedIndices = remember { mutableStateListOf<Int>() }
    var moves by remember { mutableIntStateOf(0) }
    var matches by remember { mutableIntStateOf(0) }
    var timerStarted by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isProcessing by remember { mutableStateOf(false) }
    var processTrigger by remember { mutableIntStateOf(0) }
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    var wins by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "memory")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    var hasAwarded by remember { mutableStateOf(false) }

    LaunchedEffect(timerStarted) {
        if (timerStarted) {
            while (true) {
                delay(1000)
                if (matches >= 8) break
                elapsedSeconds++
            }
        }
    }

    LaunchedEffect(processTrigger) {
        if (processTrigger > 0 && flippedIndices.size == 2) {
            val first = cards.value[flippedIndices[0]]
            val second = cards.value[flippedIndices[1]]
            if (first.symbol == second.symbol) {
                cards.value = cards.value.map {
                    if (it.id == first.id || it.id == second.id) it.copy(isMatched = true) else it
                }
                matches++
            } else {
                delay(800)
                cards.value = cards.value.map {
                    if (it.id == first.id || it.id == second.id) it.copy(isFlipped = false) else it
                }
            }
            flippedIndices.clear()
            isProcessing = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("←", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Text(strings.toolTitles["memory"] ?: "Memory Match", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards.value, key = { it.id }) { card ->
                CardItem(
                    card = card,
                    onClick = {
                        if (!isProcessing && !card.isFlipped && !card.isMatched && flippedIndices.size < 2) {
                            haptic.performIfEnabled()
                            if (!timerStarted) timerStarted = true
                            moves++
                            cards.value = cards.value.map {
                                if (it.id == card.id) it.copy(isFlipped = true) else it
                            }
                            flippedIndices.add(cards.value.indexOfFirst { it.id == card.id })
                            if (flippedIndices.size == 2) {
                                isProcessing = true
                                processTrigger++
                            }
                        }
                    }
                )
            }
        }

        if (matches == 8) {
            if (!hasAwarded) {
                hasAwarded = true
                val newWins = wins + 1
                wins = newWins
                GameStatsManager.setHighScore(ctx, "memory", newWins)
                val pts = 5
                PointsManager.addPoints(ctx, pts)
                rewardMsg = strings.gameYouEarned.format(pts)
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${strings.gameYouWin} ${strings.gameMoves.format(moves)}, ${strings.gameTime.format(elapsedSeconds)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                if (rewardMsg != null) {
                    Text(rewardMsg!!, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${strings.gameMoves.format(moves)} | ${strings.gameMatches.format(matches)} | ${strings.gameTime.format(elapsedSeconds)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(strings.gameBestScore.format(wins), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Button(
            onClick = {
                haptic.performIfEnabled()
                cards.value = (symbols + symbols).shuffled().mapIndexed { i, s -> Card(i, s, false, false) }
                flippedIndices.clear()
                moves = 0
                matches = 0
                timerStarted = false
                elapsedSeconds = 0
                isProcessing = false
                processTrigger = 0
                rewardMsg = null
                hasAwarded = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(strings.gameNewGame, fontSize = 16.sp)
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CardItem(card: Card, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 1f else 0.85f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "cardFlip",
    )
    val bgColor = if (card.isMatched) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    } else if (card.isFlipped) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }
    val contentColor = if (card.isMatched || card.isFlipped) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val borderColor = if (card.isMatched) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(if (card.isMatched) 2.dp else 0.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (card.isFlipped || card.isMatched) {
            Text(
                text = card.symbol,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
