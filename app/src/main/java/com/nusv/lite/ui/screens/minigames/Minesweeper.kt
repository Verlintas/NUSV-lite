package com.nusv.lite.ui.screens.minigames

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlin.random.Random

data class Cell(
    val hasMine: Boolean = false,
    val revealed: Boolean = false,
    val flagged: Boolean = false,
    val adjacentMines: Int = 0
)

private const val ROWS = 9
private const val COLS = 9
private const val MINES = 10

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Minesweeper(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current

    var grid by remember { mutableStateOf(emptyGrid()) }
    var gameOver by remember { mutableStateOf(false) }
    var gameWon by remember { mutableStateOf(false) }
    var firstTap by remember { mutableStateOf(true) }
    var flagMode by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var flagCount by remember { mutableIntStateOf(0) }
    var revealedCount by remember { mutableIntStateOf(0) }
    var wins by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "minesweeper")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(firstTap, gameOver, gameWon) {
        if (!firstTap && !gameOver && !gameWon) {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    fun countAdjacentMines(g: List<List<Cell>>, row: Int, col: Int): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = row + dr
                val nc = col + dc
                if (nr in 0 until ROWS && nc in 0 until COLS && g[nr][nc].hasMine) count++
            }
        }
        return count
    }

    fun placeMines(safeRow: Int, safeCol: Int) {
        val positions = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                if (r != safeRow || c != safeCol) positions.add(r to c)
            }
        }
        positions.shuffle(Random)
        val minePositions = positions.take(MINES).toSet()
        val newGrid = List(ROWS) { r ->
            List(COLS) { c ->
                if ((r to c) in minePositions) Cell(hasMine = true) else Cell()
            }
        }
        val finalGrid = newGrid.mapIndexed { r, row ->
            row.mapIndexed { c, cell ->
                if (cell.hasMine) cell
                else cell.copy(adjacentMines = countAdjacentMines(newGrid, r, c))
            }
        }
        grid = finalGrid
    }

    fun revealAllMines(currentGrid: List<MutableList<Cell>>) {
        for (rr in 0 until ROWS) {
            for (cc in 0 until COLS) {
                if (currentGrid[rr][cc].hasMine && !currentGrid[rr][cc].flagged) {
                    currentGrid[rr][cc] = currentGrid[rr][cc].copy(revealed = true)
                }
            }
        }
        grid = currentGrid.map { it.toList() }
    }

    fun chordCell(row: Int, col: Int) {
        if (gameOver || gameWon) return
        val cell = grid[row][col]
        if (!cell.revealed || cell.adjacentMines == 0) return
        var flagCountAdj = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = row + dr
                val nc = col + dc
                if (nr in 0 until ROWS && nc in 0 until COLS && grid[nr][nc].flagged) flagCountAdj++
            }
        }
        if (flagCountAdj < cell.adjacentMines) return
        val currentGrid = grid.map { it.toMutableList() }
        val stack = ArrayDeque<Pair<Int, Int>>()
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = row + dr
                val nc = col + dc
                if (nr in 0 until ROWS && nc in 0 until COLS) stack.addLast(nr to nc)
            }
        }
        while (stack.isNotEmpty()) {
            val (r, c) = stack.removeLast()
            val c2 = currentGrid[r][c]
            if (c2.revealed || c2.flagged) continue
            if (c2.hasMine) {
                gameOver = true
                haptic.performIfEnabled()
                revealAllMines(currentGrid)
                return
            }
            currentGrid[r][c] = c2.copy(revealed = true)
            revealedCount++
            if (c2.adjacentMines == 0) {
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr == 0 && dc == 0) continue
                        val nr = r + dr
                        val nc = c + dc
                        if (nr in 0 until ROWS && nc in 0 until COLS) {
                            val neighbor = currentGrid[nr][nc]
                            if (!neighbor.revealed && !neighbor.flagged) stack.addLast(nr to nc)
                        }
                    }
                }
            }
        }
        grid = currentGrid.map { it.toList() }
        val totalSafe = ROWS * COLS - MINES
        if (revealedCount == totalSafe) {
            gameWon = true
            haptic.performIfEnabled()
            val newWins = wins + 1
            wins = newWins
            GameStatsManager.setHighScore(ctx, "minesweeper", newWins)
            val pts = 5
            PointsManager.addPoints(ctx, pts)
            rewardMsg = strings.gameYouEarned.format(pts)
        }
    }

    fun revealAllMines2(currentGrid: List<MutableList<Cell>>) {
        for (rr in 0 until ROWS) {
            for (cc in 0 until COLS) {
                if (currentGrid[rr][cc].hasMine && !currentGrid[rr][cc].flagged) {
                    currentGrid[rr][cc] = currentGrid[rr][cc].copy(revealed = true)
                }
            }
        }
        grid = currentGrid.map { it.toList() }
    }

    fun revealCell(row: Int, col: Int) {
        if (gameOver || gameWon) return
        val cell = grid[row][col]
        if (cell.revealed) {
            chordCell(row, col)
            return
        }
        if (cell.flagged) return

        if (firstTap) {
            firstTap = false
            placeMines(row, col)
        }

        val currentGrid = grid.map { it.toMutableList() }
        val stack = ArrayDeque<Pair<Int, Int>>()
        stack.addLast(row to col)

        while (stack.isNotEmpty()) {
            val (r, c) = stack.removeLast()
            val c2 = currentGrid[r][c]
            if (c2.revealed || c2.flagged) continue

            if (c2.hasMine) {
                gameOver = true
                haptic.performIfEnabled()
                revealAllMines(currentGrid)
                return
            }

            currentGrid[r][c] = c2.copy(revealed = true)
            revealedCount++

            if (c2.adjacentMines == 0) {
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr == 0 && dc == 0) continue
                        val nr = r + dr
                        val nc = c + dc
                        if (nr in 0 until ROWS && nc in 0 until COLS) {
                            val neighbor = currentGrid[nr][nc]
                            if (!neighbor.revealed && !neighbor.flagged) {
                                stack.addLast(nr to nc)
                            }
                        }
                    }
                }
            }
        }

        grid = currentGrid.map { it.toList() }

        val totalSafe = ROWS * COLS - MINES
        if (revealedCount == totalSafe) {
            gameWon = true
            haptic.performIfEnabled()
            val newWins = wins + 1
            wins = newWins
            GameStatsManager.setHighScore(ctx, "minesweeper", newWins)
            val pts = 5
            PointsManager.addPoints(ctx, pts)
            rewardMsg = strings.gameYouEarned.format(pts)
        }
    }

    fun toggleFlag(row: Int, col: Int) {
        if (gameOver || gameWon) return
        val cell = grid[row][col]
        if (cell.revealed) return
        val newGrid = grid.map { it.toMutableList() }
        newGrid[row][col] = cell.copy(flagged = !cell.flagged)
        if (cell.flagged) flagCount-- else flagCount++
        grid = newGrid.map { it.toList() }
    }

    fun newGame() {
        grid = emptyGrid()
        gameOver = false
        gameWon = false
        firstTap = true
        flagMode = false
        elapsedSeconds = 0
        flagCount = 0
        revealedCount = 0
        rewardMsg = null
    }

    val numberColors = mapOf(
        1 to Color(0xFF0000FF),
        2 to Color(0xFF008000),
        3 to Color(0xFFFF0000),
        4 to Color(0xFF000080),
        5 to Color(0xFF800000),
        6 to Color(0xFF008080),
        7 to Color(0xFF000000),
        8 to Color(0xFF808080)
    )

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
            Text(strings.toolTitles["minesweeper"] ?: "Minesweeper", style = MaterialTheme.typography.headlineMedium)
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
        } else if (gameWon) {
            Text(
                text = strings.gameYouWin,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            for (row in 0 until ROWS) {
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    for (col in 0 until COLS) {
                        val cell = grid[row][col]
                        val displayText = when {
                            cell.flagged -> "\u2691"
                            !cell.revealed -> ""
                            cell.hasMine -> "\u25CF"
                            cell.adjacentMines > 0 -> cell.adjacentMines.toString()
                            else -> ""
                        }
                        val textColor = if (cell.revealed && cell.adjacentMines > 0 && !cell.hasMine) {
                            numberColors[cell.adjacentMines] ?: Color.Black
                        } else {
                            Color.Black
                        }
                        val bgColor = when {
                            cell.revealed && cell.hasMine -> Color(0xFFFF4444)
                            cell.revealed -> Color(0xFFE0E0E0)
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(bgColor, RoundedCornerShape(2.dp))
                                .border(0.5.dp, Color.Gray, RoundedCornerShape(2.dp))
                                .combinedClickable(
                                    onClick = {
                                        haptic.performIfEnabled()
                                        if (flagMode) toggleFlag(row, col)
                                        else revealCell(row, col)
                                    },
                                    onLongClick = { haptic.performIfEnabled(); toggleFlag(row, col) }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (displayText.isNotEmpty()) {
                                Text(
                                    text = displayText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${strings.gameMines.format(MINES)} | ${strings.gameFlags.format(flagCount)} | ${strings.gameTime.format(elapsedSeconds)}",
                style = MaterialTheme.typography.titleMedium,
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(strings.gameBestScore.format(wins), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (rewardMsg != null) {
                    Text(rewardMsg!!, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { haptic.performIfEnabled(); newGame() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(strings.gameNewGame, color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = { haptic.performIfEnabled(); flagMode = !flagMode },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (flagMode) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    if (flagMode) strings.gameFlagOn else strings.gameFlagOff,
                    color = if (flagMode) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun emptyGrid(): List<List<Cell>> {
    return List(ROWS) { List(COLS) { Cell() } }
}
