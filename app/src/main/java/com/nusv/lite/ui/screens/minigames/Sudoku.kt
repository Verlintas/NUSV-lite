package com.nusv.lite.ui.screens.minigames

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.GameStatsManager
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.SoundManager
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

private fun sudokuIsValid(board: Array<IntArray>, r: Int, c: Int, v: Int): Boolean {
    for (i in 0 until 9) {
        if (board[r][i] == v || board[i][c] == v) return false
    }
    val br = r / 3 * 3
    val bc = c / 3 * 3
    for (i in 0 until 3) {
        for (j in 0 until 3) {
            if (board[br + i][bc + j] == v) return false
        }
    }
    return true
}

private fun solveSudoku(board: Array<IntArray>): Boolean {
    for (r in 0 until 9) {
        for (c in 0 until 9) {
            if (board[r][c] == 0) {
                val nums = (1..9).shuffled(Random)
                for (v in nums) {
                    if (sudokuIsValid(board, r, c, v)) {
                        board[r][c] = v
                        if (solveSudoku(board)) return true
                        board[r][c] = 0
                    }
                }
                return false
            }
        }
    }
    return true
}

private fun generateSudoku(removeCount: Int): Pair<Array<IntArray>, Array<IntArray>> {
    val solution = Array(9) { IntArray(9) }
    solveSudoku(solution)
    val puzzle = solution.map { it.copyOf() }.toTypedArray()
    var removed = 0
    val positions = (0 until 81).shuffled(Random)
    for (p in positions) {
        if (removed >= removeCount) break
        val r = p / 9
        val c = p % 9
        val saved = puzzle[r][c]
        puzzle[r][c] = 0
        if (countSolutions(puzzle) == 1) {
            removed++
        } else {
            puzzle[r][c] = saved
        }
    }
    return puzzle to solution
}

private fun countSolutions(board: Array<IntArray>): Int {
    var count = 0
    fun bt(b: Array<IntArray>): Boolean {
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (b[r][c] == 0) {
                    for (v in 1..9) {
                        if (sudokuIsValid(b, r, c, v)) {
                            b[r][c] = v
                            if (bt(b)) return true
                            b[r][c] = 0
                        }
                    }
                    return false
                }
            }
        }
        count++
        return count > 1
    }
    bt(board.map { it.copyOf() }.toTypedArray())
    return count
}

@Composable
fun Sudoku(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current

    var puzzle by remember { mutableStateOf<Array<IntArray>>(emptyArray()) }
    var solution by remember { mutableStateOf<Array<IntArray>>(emptyArray()) }
    var current by remember { mutableStateOf<Array<IntArray>>(emptyArray()) }
    var notes by remember { mutableStateOf<Array<BooleanArray>>(emptyArray()) }
    var selected by remember { mutableStateOf(-1) }
    var difficulty by remember { mutableStateOf(0) }
    var solved by remember { mutableStateOf(false) }
    var solvedCount by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "sudoku")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    var checkMsg by remember { mutableStateOf<String?>(null) }
    var generating by remember { mutableStateOf(false) }
    var generatingFor by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var genJob: kotlinx.coroutines.Job? = null

    fun startNewGame() {
        genJob?.cancel()
        genJob = null
        generating = true
        generatingFor = difficulty
        genJob = scope.launch {
            val removeCount = when (difficulty) {
                0 -> 38
                1 -> 48
                else -> 56
            }
            val (p, s) = withContext(Dispatchers.Default) { generateSudoku(removeCount) }
            puzzle = p.map { it.copyOf() }.toTypedArray()
            solution = s
            current = p.map { it.copyOf() }.toTypedArray()
            notes = Array(9) { BooleanArray(9) }
            selected = -1
            solved = false
            rewardMsg = null
            checkMsg = null
            generating = false
        }
    }

    LaunchedEffect(Unit) { startNewGame() }

    fun inputNumber(v: Int) {
        if (solved || selected < 0) return
        val r = selected / 9
        val c = selected % 9
        if (puzzle[r][c] != 0) return
        if (v == 0) {
            current[r][c] = 0
            notes[r][c] = false
            SoundManager.playTap()
            checkMsg = null
            return
        }
        notes[r][c] = false
        current[r][c] = v
        SoundManager.playTap()
        checkMsg = null
        if (current.all { row -> row.all { it != 0 } }) {
            val isFull = current.indices.all { i -> current[i].indices.all { j -> current[i][j] == solution[i][j] } }
            if (isFull) {
                solved = true
                solvedCount++
                GameStatsManager.setHighScore(ctx, "sudoku", solvedCount)
                val pts = 40
                PointsManager.addPoints(ctx, pts)
                rewardMsg = strings.gameYouEarned.format(pts)
                SoundManager.playWin()
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            } else {
                checkMsg = strings.gameWrong
                SoundManager.playError()
            }
        }
    }

    fun checkBoard() {
        if (solved) return
        val wrong = current.indices.any { i ->
            current[i].indices.any { j ->
                current[i][j] != 0 && current[i][j] != solution[i][j]
            }
        }
        checkMsg = if (wrong) strings.gameWrong else strings.gameCorrect
        if (wrong) SoundManager.playError() else SoundManager.playSuccess()
    }

    val isSudokuOrca = MaterialTheme.colorScheme.background == Color.Black
    val suBw = if (isSudokuOrca) 1.dp else 0.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBack() }) { Text("‹") }
            Text("Sudoku", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = { haptic.performIfEnabled(); startNewGame() }) { Text(strings.gameNewGame) }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(strings.gameLevelEasy, strings.gameLevelMedium, strings.gameLevelHard).forEachIndexed { idx, label ->
                Box(
                    modifier = Modifier
                        .background(
                            if (difficulty == idx) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            difficulty = idx
                            startNewGame()
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (difficulty == idx) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        if (generating) {
            Spacer(Modifier.height(40.dp))
            Text(strings.gameGenerating, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(40.dp))
        } else if (current.isEmpty()) {
            Spacer(Modifier.height(100.dp))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .border(suBw, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                (0 until 9).forEach { r ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        (0 until 9).forEach { c ->
                            val idx = r * 9 + c
                            val isGiven = puzzle[r][c] != 0
                            val value = current[r][c]
                            val isSelected = selected == idx
                            val isSame = selected >= 0 &&
                                (r == selected / 9 || c == selected % 9 ||
                                    (r / 3 == selected / 9 / 3 && c / 3 == selected % 9 / 3))
                            val isWrong = value != 0 && puzzle[r][c] == 0 && solution[r][c] != value
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                                            isSame && value != 0 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                            isSame -> MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        haptic.performIfEnabled()
                                        selected = idx
                                    }
                                    .then(
                                        if (r % 3 == 2 && r < 8) Modifier.padding(bottom = 1.dp) else Modifier
                                    )
                                    .then(
                                        if (c % 3 == 2 && c < 8) Modifier.padding(end = 1.dp) else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (value != 0) {
                                    Text(
                                        text = "$value",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isWrong -> MaterialTheme.colorScheme.error
                                            isGiven -> MaterialTheme.colorScheme.onSurface
                                            else -> MaterialTheme.colorScheme.primary
                                        }
                                    )
                                } else if (notes[r][c]) {
                                    Text("·", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        checkMsg?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelMedium,
                color = if (it == strings.gameCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
        rewardMsg?.let {
            Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "${strings.gameWins}: $solvedCount",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { haptic.performIfEnabled(); checkBoard() }) { Text(strings.gameCheck) }
            Button(onClick = { haptic.performIfEnabled(); inputNumber(0) }) { Text(strings.gameErase) }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (1..9).forEach { v ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .clickable {
                            haptic.performIfEnabled()
                            inputNumber(v)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("$v", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (solved) {
            Spacer(Modifier.height(8.dp))
            Button(onClick = { haptic.performIfEnabled(); startNewGame() }) { Text(strings.gamePlayAgain) }
        }

        Spacer(Modifier.height(16.dp))
    }
}
