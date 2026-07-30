package com.nusv.lite.ui.screens.minigames

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.nusv.lite.util.GameStatsManager
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class SimonColor(val name: String, val color: Color, val glow: Color)

private val colors = listOf(
    SimonColor("Green", Color(0xFF4CAF50), Color(0xFF81C784)),
    SimonColor("Red", Color(0xFFE53935), Color(0xFFEF5350)),
    SimonColor("Blue", Color(0xFF1E88E5), Color(0xFF42A5F5)),
    SimonColor("Yellow", Color(0xFFFFD600), Color(0xFFFFEE58)),
)

@Composable
fun SimonGame(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    val sequence = remember { mutableStateListOf<Int>() }
    var playerIndex by remember { mutableIntStateOf(0) }
    var isShowing by remember { mutableStateOf(true) }
    var highlightedIndex by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var isPlayerTurn by remember { mutableStateOf(false) }
    var highScore by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "simon")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    var hasAwarded by remember { mutableStateOf(false) }

    fun endGame() {
        gameOver = true
        if (!hasAwarded) {
            hasAwarded = true
            GameStatsManager.setHighScore(ctx, "simon", score)
            highScore = maxOf(highScore, score)
            val pts = score.coerceAtLeast(1)
            PointsManager.addPoints(ctx, pts)
            rewardMsg = strings.gameYouEarned.format(pts)
        }
    }

    fun startRound() {
        sequence.add(Random.nextInt(4))
        playerIndex = 0
        isShowing = true
        isPlayerTurn = false
    }

    LaunchedEffect(Unit) { startRound() }

    fun showSequence() {
        isShowing = true
        isPlayerTurn = false
    }

    LaunchedEffect(sequence.size) {
        if (sequence.isEmpty()) return@LaunchedEffect
        isShowing = true
        isPlayerTurn = false
        val baseDelay = (400 - minOf(sequence.size * 20, 200)).coerceAtLeast(200)
        for (i in sequence.indices) {
            highlightedIndex = sequence[i]
            haptic.performIfEnabled()
            delay(baseDelay.toLong())
            highlightedIndex = -1
            delay((baseDelay / 2).toLong())
        }
        isShowing = false
        isPlayerTurn = true
    }

    LaunchedEffect(isPlayerTurn) {
        if (isPlayerTurn && !gameOver) {
            delay(5000)
            if (isPlayerTurn && !gameOver) {
                endGame()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    fun onPlayerTap(idx: Int) {
        if (!isPlayerTurn || isShowing || gameOver) return
        haptic.performIfEnabled()
        if (idx == sequence[playerIndex]) {
            highlightedIndex = idx
            playerIndex++
            if (playerIndex >= sequence.size) {
                score++
                startRound()
            }
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            endGame()
        }
    }

    fun reset() {
        sequence.clear()
        score = 0
        gameOver = false
        isPlayerTurn = false
        isShowing = false
        highlightedIndex = -1
        rewardMsg = null
        hasAwarded = false
        startRound()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center,
            ) { Text("\u2190", style = MaterialTheme.typography.titleLarge) }
            Spacer(Modifier.width(12.dp))
            Text(strings.toolTitles["simon"] ?: "Simon Says", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("$score", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(strings.gameBestScore.format(highScore), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (rewardMsg != null) {
                    Text(rewardMsg!!, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (gameOver) strings.gameGameOver else if (isShowing) strings.gameWatch else strings.gameYourTurn,
            style = MaterialTheme.typography.titleLarge,
            color = if (gameOver) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (i in 0..1) {
                        val c = colors[i]
                        val isHighlighted = highlightedIndex == i
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(
                                    if (isHighlighted) c.glow else c.color.copy(alpha = 0.7f),
                                    RoundedCornerShape(20.dp),
                                )
                                .clickable { onPlayerTap(i) },
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (i in 2..3) {
                        val c = colors[i]
                        val isHighlighted = highlightedIndex == i
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(
                                    if (isHighlighted) c.glow else c.color.copy(alpha = 0.7f),
                                    RoundedCornerShape(20.dp),
                                )
                                .clickable { onPlayerTap(i) },
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (gameOver) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); reset() }
                    .padding(horizontal = 32.dp, vertical = 12.dp),
            ) { Text(strings.gamePlayAgain, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold) }
        }
    }
}
