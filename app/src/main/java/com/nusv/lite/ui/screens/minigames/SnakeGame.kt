package com.nusv.lite.ui.screens.minigames

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Point(val x: Int, val y: Int)

private enum class Direction { UP, DOWN, LEFT, RIGHT }

@Composable
fun SnakeGame(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    val cols = 16
    val rows = 24
    var snake by remember { mutableStateOf(listOf(Point(8, 12), Point(7, 12), Point(6, 12))) }
    var food by remember { mutableStateOf(Point(12, 12)) }
    var direction by remember { mutableStateOf(Direction.RIGHT) }
    var nextDirection by remember { mutableStateOf(Direction.RIGHT) }
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var foodAnim by remember { mutableStateOf(0f) }
    var highScore by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "snake")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    var hasAwarded by remember { mutableStateOf(false) }

    val particles = remember { mutableStateListOf<Pair<Offset, Float>>() }

    fun spawnFood() {
        val occupied = snake.toSet()
        val available = (0 until cols).flatMap { x ->
            (0 until rows).map { y -> Point(x, y) }
        }.filter { it !in occupied }
        if (available.isNotEmpty()) food = available.random()
    }

    fun reset() {
        snake = listOf(Point(8, 12), Point(7, 12), Point(6, 12))
        direction = Direction.RIGHT
        nextDirection = Direction.RIGHT
        score = 0
        gameOver = false
        isPaused = false
        particles.clear()
        spawnFood()
        rewardMsg = null
        hasAwarded = false
    }

    LaunchedEffect(Unit) { spawnFood() }

    LaunchedEffect(gameOver, isPaused) {
        if (gameOver || isPaused) return@LaunchedEffect
        while (true) {
            delay((120 - minOf(score, 10) * 8).toLong().coerceAtLeast(60))
            direction = nextDirection
            val head = snake.first()
            val newHead = when (direction) {
                Direction.UP -> Point(head.x, head.y - 1)
                Direction.DOWN -> Point(head.x, head.y + 1)
                Direction.LEFT -> Point(head.x - 1, head.y)
                Direction.RIGHT -> Point(head.x + 1, head.y)
            }
            if (newHead.x < 0 || newHead.x >= cols || newHead.y < 0 || newHead.y >= rows || newHead in snake) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                gameOver = true
                if (!hasAwarded) {
                    hasAwarded = true
                    GameStatsManager.setHighScore(ctx, "snake", score)
                    highScore = maxOf(highScore, score)
                    val pts = score.coerceAtLeast(1)
                    PointsManager.addPoints(ctx, pts)
                    rewardMsg = strings.gameYouEarned.format(pts)
                }
                return@LaunchedEffect
            }
            val newSnake = mutableListOf(newHead)
            newSnake.addAll(snake)
            if (newHead == food) {
                score++
                haptic.performIfEnabled()
                particles.add(Offset((food.x + 0.5f) / cols, (food.y + 0.5f) / rows) to 1f)
                if (particles.size > 10) particles.removeAt(0)
                spawnFood()
            } else {
                newSnake.removeAt(newSnake.size - 1)
            }
            snake = newSnake
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
            Text(strings.toolTitles["snake"] ?: "Snake", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            Text("$score", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(strings.gameBestScore.format(highScore), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (rewardMsg != null) {
                    Text(rewardMsg!!, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(8.dp),
        ) {
            if (gameOver) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(strings.gameGameOver, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(strings.gameScore.format(score), style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                                .clickable { haptic.performIfEnabled(); reset() }
                                .padding(horizontal = 32.dp, vertical = 12.dp),
                        ) { Text(strings.gamePlayAgain, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold) }
                    }
                }
            }
            if (isPaused && !gameOver) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(strings.gamePaused, style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            val primary = MaterialTheme.colorScheme.primary
            val tertiary = MaterialTheme.colorScheme.tertiary

            Canvas(
                modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val cellW = size.width.toFloat() / cols
                        val cellH = size.height.toFloat() / rows
                        if (gameOver || isPaused) return@detectTapGestures
                        val cx = (offset.x / cellW).toInt()
                        val cy = (offset.y / cellH).toInt()
                        val head = snake.first()
                        val dx = cx - head.x
                        val dy = cy - head.y
                        if (abs(dx) > abs(dy)) {
                            if (dx > 0 && direction != Direction.LEFT) nextDirection = Direction.RIGHT
                            else if (dx < 0 && direction != Direction.RIGHT) nextDirection = Direction.LEFT
                        } else {
                            if (dy > 0 && direction != Direction.UP) nextDirection = Direction.DOWN
                            else if (dy < 0 && direction != Direction.DOWN) nextDirection = Direction.UP
                        }
                    }
                },
            ) {
                val cellW = size.width / cols
                val cellH = size.height / rows
                val primaryDim = primary.copy(alpha = 0.6f)

                for (i in snake.indices) {
                    val p = snake[i]
                    val alpha = 1f - (i.toFloat() / snake.size) * 0.5f
                    val color = if (i == 0) primary else primaryDim.copy(alpha = alpha)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(p.x * cellW + 1, p.y * cellH + 1),
                        size = Size(cellW - 2, cellH - 2),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                    )
                }

                val pulse = sin(System.currentTimeMillis() / 200.0).toFloat() * 0.2f + 0.8f
                drawCircle(
                    color = tertiary.copy(alpha = pulse),
                    radius = minOf(cellW, cellH) * 0.35f,
                    center = Offset((food.x + 0.5f) * cellW, (food.y + 0.5f) * cellH),
                )

                for ((pos, _) in particles) {
                    val px = pos.x * size.width
                    val py = pos.y * size.height
                    for (a in 0 until 6) {
                        val angle = a * 60f + System.currentTimeMillis() / 50f
                        val r = 20f + (System.currentTimeMillis() % 300) / 300f * 30f
                        drawCircle(
                            color = primary.copy(alpha = 0.5f),
                            radius = 3f,
                            center = Offset(px + cos(angle) * r, py + sin(angle) * r),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); if (!gameOver) isPaused = !isPaused },
                contentAlignment = Alignment.Center,
            ) { Text(if (isPaused) "\u25B6" else "\u23F8", fontSize = 24.sp) }
        }
    }
}
