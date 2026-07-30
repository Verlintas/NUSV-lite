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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WhackAMole(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    val gridSize = 3
    val moles = remember { mutableStateListOf<Int>() }
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var gameRunning by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var lastHit by remember { mutableIntStateOf(-1) }
    var hitAnim by remember { mutableStateOf(0) }
    var highScore by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "whack")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    val hitScale by animateFloatAsState(
        targetValue = if (hitAnim > 0) 1.3f else 1f,
        animationSpec = tween(150),
        label = "hitScale",
    )

    LaunchedEffect(lastHit) {
        if (lastHit >= 0) {
            hitAnim++
            delay(150)
        }
    }

    LaunchedEffect(gameRunning) {
        if (!gameRunning) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (moles.size < 3 && Random.nextFloat() < 0.5f) {
                val pos = Random.nextInt(gridSize * gridSize)
                if (pos !in moles) moles.add(pos)
            }
            val iter = moles.iterator()
            while (iter.hasNext()) {
                if (Random.nextFloat() < 0.3f) iter.remove()
            }
        }
        gameRunning = false
        gameOver = true
        GameStatsManager.setHighScore(ctx, "whack", score)
        highScore = maxOf(highScore, score)
        val pts = (score / 2).coerceAtLeast(1)
        PointsManager.addPoints(ctx, pts)
        rewardMsg = strings.gameYouEarned.format(pts)
    }

    fun startGame() {
        score = 0
        timeLeft = 30
        moles.clear()
        moles.add(Random.nextInt(gridSize * gridSize))
        gameRunning = true
        gameOver = false
        rewardMsg = null
    }

    fun onMoleTap(idx: Int) {
        if (!gameRunning) return
        if (idx in moles) {
            haptic.performIfEnabled()
            moles.remove(idx)
            score++
            lastHit = idx
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
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
            Text(strings.toolTitles["whack"] ?: "Whack-a-Mole", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("$score", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                if (gameRunning) Text("${timeLeft}s", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (!gameRunning) {
                    Text(strings.gameBestScore.format(highScore), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (rewardMsg != null) {
                        Text(rewardMsg!!, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (!gameRunning && !gameOver) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(strings.toolTitles["whack"] ?: "Whack-a-Mole", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(strings.gameTapMoles, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                            .clickable { haptic.performIfEnabled(); startGame() }
                            .padding(horizontal = 40.dp, vertical = 14.dp),
                    ) { Text(strings.gameStart, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                }
            } else if (gameOver) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(strings.gameTimeUp, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Text(strings.gameScore.format(score), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                            .clickable { haptic.performIfEnabled(); startGame() }
                            .padding(horizontal = 32.dp, vertical = 12.dp),
                    ) { Text(strings.gamePlayAgain, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold) }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    for (r in 0 until gridSize) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (c in 0 until gridSize) {
                                val idx = r * gridSize + c
                                val hasMole = idx in moles
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(
                                            if (hasMole) Color(0xFF8D6E63) else Color(0xFF5D4037),
                                            RoundedCornerShape(12.dp),
                                        )
                                        .clickable { onMoleTap(idx) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (hasMole) {
                                        Text("\uD83D\uDC3B", fontSize = 36.sp, modifier = Modifier.scale(hitScale))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
