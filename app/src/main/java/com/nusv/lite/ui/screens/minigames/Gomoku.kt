package com.nusv.lite.ui.screens.minigames

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.GameStatsManager
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.SoundManager
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay
import kotlin.math.max

private const val GOMOKU_SIZE = 15

private data class GomokuState(
    val board: List<Int> = List(GOMOKU_SIZE * GOMOKU_SIZE) { 0 },
    val turn: Int = 1,
    val winner: Int = 0,
    val lastMove: Int = -1,
)

private fun IndexOf(r: Int, c: Int) = r * GOMOKU_SIZE + c

private fun checkWin(board: List<Int>, index: Int): Boolean {
    val player = board[index]
    if (player == 0) return false
    val r = index / GOMOKU_SIZE
    val c = index % GOMOKU_SIZE
    val dirs = listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)
    for ((dr, dc) in dirs) {
        var count = 1
        for (s in 1..4) {
            val nr = r + dr * s
            val nc = c + dc * s
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) count++ else break
        }
        for (s in 1..4) {
            val nr = r - dr * s
            val nc = c - dc * s
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) count++ else break
        }
        if (count >= 5) return true
    }
    return false
}

private fun lineScore(count: Int, openEnds: Int): Int = when {
    count >= 5 -> 10000000
    count == 4 -> when (openEnds) { 2 -> 1000000; 1 -> 100000; else -> 0 }
    count == 3 -> when (openEnds) { 2 -> 50000; 1 -> 5000; else -> 0 }
    count == 2 -> when (openEnds) { 2 -> 1000; 1 -> 100; else -> 0 }
    count == 1 -> when (openEnds) { 2 -> 50; 1 -> 10; else -> 0 }
    else -> 0
}

private fun scorePoint(board: List<Int>, index: Int, player: Int): Int {
    val r = index / GOMOKU_SIZE
    val c = index % GOMOKU_SIZE
    var score = 0
    val dirs = listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)
    for ((dr, dc) in dirs) {
        var count = 1
        var openEnds = 0
        var f = 1
        while (f <= 4) {
            val nr = r + dr * f
            val nc = c + dc * f
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) {
                count++
                f++
            } else {
                if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) openEnds++
                break
            }
        }
        var b = 1
        while (b <= 4) {
            val nr = r - dr * b
            val nc = c - dc * b
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) {
                count++
                b++
            } else {
                if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) openEnds++
                break
            }
        }
        score += lineScore(count, openEnds)
    }
    return score
}

private fun immediateWins(board: List<Int>, player: Int): List<Int> {
    val res = mutableListOf<Int>()
    for (r in 0 until GOMOKU_SIZE) {
        for (c in 0 until GOMOKU_SIZE) {
            val idx = IndexOf(r, c)
            if (board[idx] != 0) continue
            val nb = board.toMutableList()
            nb[idx] = player
            if (checkWin(nb, idx)) res.add(idx)
        }
    }
    return res
}

private fun fourThreatPoints(board: List<Int>, player: Int): List<Int> {
    val res = mutableListOf<Int>()
    for (r in 0 until GOMOKU_SIZE) {
        for (c in 0 until GOMOKU_SIZE) {
            val idx = IndexOf(r, c)
            if (board[idx] != 0) continue
            val nb = board.toMutableList()
            nb[idx] = player
            if (scorePoint(nb, idx, player) >= 1000000) res.add(idx)
        }
    }
    return res
}

private fun urgentMove(board: List<Int>, mover: Int, other: Int): Int? {
    immediateWins(board, mover).firstOrNull()?.let { return it }
    immediateWins(board, other).firstOrNull()?.let { return it }
    fourThreatPoints(board, mover).firstOrNull()?.let { return it }
    fourThreatPoints(board, other).firstOrNull()?.let { return it }
    return null
}

private fun evaluateBoard(board: List<Int>, me: Int, opponent: Int): Long {
    var score = 0L
    val dirs = listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)
    for (r in 0 until GOMOKU_SIZE) {
        for (c in 0 until GOMOKU_SIZE) {
            val v = board[IndexOf(r, c)]
            if (v == 0) continue
            for ((dr, dc) in dirs) {
                val br = r - dr
                val bc = c - dc
                if (br in 0 until GOMOKU_SIZE && bc in 0 until GOMOKU_SIZE && board[IndexOf(br, bc)] == v) continue
                var count = 1
                var openEnds = 0
                var f = 1
                while (f <= 4) {
                    val nr = r + dr * f
                    val nc = c + dc * f
                    if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == v) {
                        count++
                        f++
                    } else {
                        if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) openEnds++
                        break
                    }
                }
                if (br in 0 until GOMOKU_SIZE && bc in 0 until GOMOKU_SIZE && board[IndexOf(br, bc)] == 0) openEnds++
                val s = lineScore(count, openEnds)
                if (v == me) score += s else score -= s
            }
        }
    }
    return score
}

private fun findBestMove(board: List<Int>, me: Int, opponent: Int): Int {
    val empty = mutableListOf<Int>()
    for (r in 0 until GOMOKU_SIZE) {
        for (c in 0 until GOMOKU_SIZE) {
            val idx = IndexOf(r, c)
            if (board[idx] != 0) continue
            var near = false
            for (dr in -2..2) {
                for (dc in -2..2) {
                    val nr = r + dr
                    val nc = c + dc
                    if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] != 0) near = true
                }
            }
            if (near) empty.add(idx)
        }
    }
    if (empty.isEmpty()) return -1
    if (empty.size == GOMOKU_SIZE * GOMOKU_SIZE) return IndexOf(7, 7)

    urgentMove(board, me, opponent)?.let { return it }

    var bestIdx = empty.first()
    var bestScore = Long.MIN_VALUE
    for (idx in empty) {
        val nb = board.toMutableList()
        nb[idx] = me
        val oppReply = urgentMove(nb, opponent, me)
        if (oppReply != null) {
            val nb2 = nb.toMutableList()
            nb2[oppReply] = opponent
            val s = evaluateBoard(nb2, me, opponent)
            if (s > bestScore) {
                bestScore = s
                bestIdx = idx
            }
            continue
        }
        val oppCandidates = empty.filter { it != idx }.sortedByDescending { oidx ->
            val nb2 = nb.toMutableList()
            nb2[oidx] = opponent
            scorePoint(nb2, oidx, opponent).toLong() + scorePoint(nb2, oidx, me)
        }.take(5)
        var worst = Long.MAX_VALUE
        for (oidx in oppCandidates) {
            val nb2 = nb.toMutableList()
            nb2[oidx] = opponent
            val s = evaluateBoard(nb2, me, opponent)
            if (s < worst) worst = s
        }
        if (worst > bestScore) {
            bestScore = worst
            bestIdx = idx
        }
    }
    return bestIdx
}

@Composable
fun Gomoku(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current

    var state by remember { mutableStateOf(GomokuState()) }
    var wins by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "gomoku")) }
    var aiThinking by remember { mutableStateOf(false) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }
    var gameId by remember { mutableIntStateOf(0) }

    val me = 1
    val ai = 2

    fun finish(winner: Int) {
        state = state.copy(winner = winner)
        if (winner == me) {
            SoundManager.playWin()
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            wins++
            GameStatsManager.setHighScore(ctx, "gomoku", wins)
            val pts = 50
            PointsManager.addPoints(ctx, pts)
            rewardMsg = strings.gameYouEarned.format(pts)
        } else if (winner == ai) {
            SoundManager.playError()
        } else {
            SoundManager.playSuccess()
        }
    }

    fun playMove(idx: Int): Boolean {
        if (state.winner != 0 || state.board[idx] != 0) return false
        val nb = state.board.toMutableList()
        nb[idx] = state.turn
        val won = checkWin(nb, idx)
        val next = if (won) state.turn else 3 - state.turn
        val newState = state.copy(board = nb, turn = next, lastMove = idx, winner = if (won) state.turn else 0)
        state = newState
        if (won) finish(state.turn)
        return won
    }

    LaunchedEffect(state.turn, state.winner, gameId) {
        if (state.winner == 0 && state.turn == ai && state.board.any { it != 0 }) {
            aiThinking = true
            delay(400)
            val best = findBestMove(state.board, ai, me)
            if (best >= 0) {
                val nb = state.board.toMutableList()
                nb[best] = ai
                val won = checkWin(nb, best)
                state = state.copy(board = nb, turn = if (won) state.turn else me, lastMove = best, winner = if (won) ai else 0)
                if (won) finish(ai)
                SoundManager.playTap()
            } else {
                state = state.copy(winner = -1)
                finish(-1)
            }
            aiThinking = false
        }
    }

    fun restart() {
        state = GomokuState()
        rewardMsg = null
        gameId++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBack() }) { Text("‹") }
            Text("Gomoku", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = { haptic.performIfEnabled(); restart() }) { Text(strings.gameNewGame) }
        }

        val statusText = when {
            state.winner == me -> "${strings.gameYouWin} (●)"
            state.winner == ai -> strings.gameGameOver
            state.winner == -1 -> strings.gameDraw
            aiThinking -> strings.gameAiThinking
            state.turn == me -> strings.gameYourTurn
            else -> strings.gameAiThinking
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelLarge,
            color = if (state.winner == me) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
        rewardMsg?.let {
            Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        var boardSizePx by remember { mutableStateOf(0f) }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .background(Color(0xFFD7A85A), RoundedCornerShape(4.dp))
                .onSizeChanged { boardSizePx = it.width.toFloat() }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val cellPx = boardSizePx / GOMOKU_SIZE
                        if (cellPx <= 0f) return@detectTapGestures
                        val r = (offset.y / cellPx).toInt().coerceIn(0, GOMOKU_SIZE - 1)
                        val c = (offset.x / cellPx).toInt().coerceIn(0, GOMOKU_SIZE - 1)
                        if (state.turn == me && state.winner == 0 && !aiThinking) {
                            haptic.performIfEnabled()
                            playMove(IndexOf(r, c))
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLines()
                state.board.forEachIndexed { idx, v ->
                    if (v != 0) {
                        val cell = size.width / GOMOKU_SIZE
                        val r = idx / GOMOKU_SIZE
                        val c = idx % GOMOKU_SIZE
                        val cx = c * cell + cell / 2
                        val cy = r * cell + cell / 2
                        val radius = cell * 0.38f
                        if (v == 1) {
                            drawCircle(Color.Black, radius, Offset(cx, cy))
                            if (idx == state.lastMove) {
                                drawCircle(Color.White, radius * 0.35f, Offset(cx, cy))
                            }
                        } else {
                            drawCircle(Color.White, radius, Offset(cx, cy))
                            if (idx == state.lastMove) {
                                drawCircle(Color.Black, radius * 0.35f, Offset(cx, cy))
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (state.winner != 0) {
            Button(onClick = { haptic.performIfEnabled(); restart() }) { Text(strings.gamePlayAgain) }
            Spacer(Modifier.height(8.dp))
        }

        Text(
            text = strings.gameWinsCount.format(wins),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
    }
}

private fun DrawScope.drawLines() {
    val cell = size.width / GOMOKU_SIZE
    val color = Color(0xFF5D3A1A)
    for (i in 0 until GOMOKU_SIZE) {
        val p = i * cell + cell / 2
        drawLine(color, Offset(p, cell / 2), Offset(p, size.height - cell / 2), strokeWidth = 1.5f)
        drawLine(color, Offset(cell / 2, p), Offset(size.width - cell / 2, p), strokeWidth = 1.5f)
    }
}
