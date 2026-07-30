package com.nusv.lite.ui.screens.minigames

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.GameStatsManager
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun Game2048(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    var grid by remember { mutableStateOf(emptyGrid()) }
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var hasWon by remember { mutableStateOf(false) }
    var highScore by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "2048")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }

    fun slideLine(line: List<Int>): Pair<List<Int>, Int> {
        val filtered = line.filter { it != 0 }.toMutableList()
        var s = 0
        var i = 0
        while (i < filtered.size - 1) {
            if (filtered[i] == filtered[i + 1]) {
                filtered[i] *= 2
                s += filtered[i]
                filtered.removeAt(i + 1)
            }
            i++
        }
        while (filtered.size < 4) filtered.add(0)
        return filtered.toList() to s
    }

    fun spawnNewTile() {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                if (grid[r][c] == 0) empty.add(r to c)
            }
        }
        if (empty.isEmpty()) return
        val (r, c) = empty.random()
        val newGrid = grid.map { it.toMutableList() }
        newGrid[r][c] = if (Random.nextInt(10) == 0) 4 else 2
        grid = newGrid.map { it.toList() }
    }

    fun checkGameOver() {
        for (r in 0 until 4) {
            for (c in 0 until 4) {
                if (grid[r][c] == 0) return
                if (c < 3 && grid[r][c] == grid[r][c + 1]) return
                if (r < 3 && grid[r][c] == grid[r + 1][c]) return
            }
        }
        gameOver = true
        val pts = (score / 100).coerceAtLeast(1)
        PointsManager.addPoints(ctx, pts)
        GameStatsManager.setHighScore(ctx, "2048", score)
        highScore = maxOf(highScore, score)
        rewardMsg = strings.gameYouEarned.format(pts)
    }

    fun moveLeft() {
        if (gameOver) return
        var totalScore = 0
        var changed = false
        val newGrid = grid.map { row ->
            val (newRow, s) = slideLine(row)
            totalScore += s
            if (newRow != row) changed = true
            newRow
        }
        if (changed) {
            if (newGrid.flatten().any { it == 2048 }) hasWon = true
            grid = newGrid
            score += totalScore
            spawnNewTile()
            checkGameOver()
        }
    }

    fun moveRight() {
        if (gameOver) return
        var totalScore = 0
        var changed = false
        val newGrid = grid.map { row ->
            val (newRow, s) = slideLine(row.reversed())
            totalScore += s
            val finalRow = newRow.reversed()
            if (finalRow != row) changed = true
            finalRow
        }
        if (changed) {
            if (newGrid.flatten().any { it == 2048 }) hasWon = true
            grid = newGrid
            score += totalScore
            spawnNewTile()
            checkGameOver()
        }
    }

    fun moveUp() {
        if (gameOver) return
        var totalScore = 0
        var changed = false
        val mutableGrid = grid.map { it.toMutableList() }
        for (col in 0 until 4) {
            val column = List(4) { grid[it][col] }
            val (newCol, s) = slideLine(column)
            totalScore += s
            for (row in 0 until 4) {
                if (mutableGrid[row][col] != newCol[row]) changed = true
                mutableGrid[row][col] = newCol[row]
            }
        }
        if (changed) {
            if (mutableGrid.flatten().any { it == 2048 }) hasWon = true
            grid = mutableGrid.map { it.toList() }
            score += totalScore
            spawnNewTile()
            checkGameOver()
        }
    }

    fun moveDown() {
        if (gameOver) return
        var totalScore = 0
        var changed = false
        val mutableGrid = grid.map { it.toMutableList() }
        for (col in 0 until 4) {
            val column = List(4) { grid[3 - it][col] }
            val (newCol, s) = slideLine(column)
            totalScore += s
            for (row in 0 until 4) {
                if (mutableGrid[row][col] != newCol[3 - row]) changed = true
                mutableGrid[row][col] = newCol[3 - row]
            }
        }
        if (changed) {
            if (mutableGrid.flatten().any { it == 2048 }) hasWon = true
            grid = mutableGrid.map { it.toList() }
            score += totalScore
            spawnNewTile()
            checkGameOver()
        }
    }

    fun newGame() {
        grid = emptyGrid()
        score = 0
        gameOver = false
        hasWon = false
        rewardMsg = null
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 48.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Text("2048", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(strings.gameScore.format(score), style = MaterialTheme.typography.titleMedium)
                Text(strings.gameBestScore.format(highScore), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (rewardMsg != null) {
                Spacer(Modifier.width(8.dp))
                Text(rewardMsg!!, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        if (gameOver) {
            Text(
                text = strings.gameGameOver,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        } else if (hasWon) {
            Text(
                text = strings.gameYouWin,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color(0xFFBBADA0), RoundedCornerShape(8.dp))
                .padding(4.dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        if (abs(dragAmount.x) > 30 || abs(dragAmount.y) > 30) {
                            if (abs(dragAmount.x) > abs(dragAmount.y)) {
                                if (dragAmount.x > 0) moveRight() else moveLeft()
                            } else {
                                if (dragAmount.y > 0) moveDown() else moveUp()
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (row in 0 until 4) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (col in 0 until 4) {
                            Tile(value = grid[row][col])
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { haptic.performIfEnabled(); newGame() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(strings.gameNewGame, color = MaterialTheme.colorScheme.onPrimary)
            }
            if (gameOver || hasWon) {
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = { haptic.performIfEnabled(); newGame() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(strings.gamePlayAgain, color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}

private fun emptyGrid(): List<List<Int>> {
    val g = MutableList(4) { MutableList(4) { 0 } }
    fun placeRandom() {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (g[i][j] == 0) empty.add(i to j)
            }
        }
        if (empty.isNotEmpty()) {
            val (r, c) = empty.random()
            g[r][c] = if (Random.nextInt(10) == 0) 4 else 2
        }
    }
    placeRandom()
    placeRandom()
    return g.map { it.toList() }
}

@Composable
private fun Tile(value: Int) {
    val scale by animateFloatAsState(
        targetValue = if (value > 0) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "tileScale",
    )
    val bgColor = when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color(0xFF3C3A32)
    }
    val textColor = if (value <= 4 && value != 0) Color(0xFF776E65) else Color.White
    val fontSize = when {
        value < 100 -> 32.sp
        value < 1000 -> 28.sp
        value < 10000 -> 24.sp
        else -> 20.sp
    }

    Box(
        modifier = Modifier
            .size(72.dp)
            .scale(scale)
            .background(bgColor, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (value > 0) {
            Text(
                text = value.toString(),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
