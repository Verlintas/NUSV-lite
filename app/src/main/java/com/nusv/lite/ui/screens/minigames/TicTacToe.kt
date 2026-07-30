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
import kotlinx.coroutines.launch

private const val BOARD_SIZE = 3

private sealed class Player {
    data object X : Player()
    data object O : Player()
}

private sealed class GameState {
    data object Playing : GameState()
    data class Won(val winner: Player, val line: List<Int>) : GameState()
    data object Draw : GameState()
}

@Composable
fun TicTacToe(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    var board by remember { mutableStateOf(Array(BOARD_SIZE * BOARD_SIZE) { null as Player? }) }
    var currentPlayer by remember { mutableStateOf<Player>(Player.X) }
    var gameState by remember { mutableStateOf<GameState>(GameState.Playing) }
    var wins by remember { mutableIntStateOf(GameStatsManager.getHighScore(ctx, "tictactoe")) }
    var rewardMsg by remember { mutableStateOf<String?>(null) }

    fun checkGameState(): GameState {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, b, c) = line
            val cell = board[a]
            if (cell != null && cell == board[b] && cell == board[c]) {
                return GameState.Won(cell, line)
            }
        }
        if (board.all { it != null }) return GameState.Draw
        return GameState.Playing
    }

    fun onCellClick(index: Int) {
        if (gameState !is GameState.Playing) return
        if (board[index] != null) return
        haptic.performIfEnabled()
        val newBoard = board.copyOf()
        newBoard[index] = currentPlayer
        board = newBoard
        val state = checkGameState()
        gameState = state
        if (state is GameState.Playing) {
            currentPlayer = when (currentPlayer) {
                is Player.X -> Player.O
                is Player.O -> Player.X
            }
        } else {
            val pts = if (state is GameState.Won) 3 else 1
            PointsManager.addPoints(ctx, pts)
            val newWins = if (state is GameState.Won) wins + 1 else wins
            if (state is GameState.Won) {
                GameStatsManager.setHighScore(ctx, "tictactoe", newWins)
                wins = newWins
            }
            rewardMsg = strings.gameYouEarned.format(pts)
        }
    }

    fun reset() {
        board = Array(BOARD_SIZE * BOARD_SIZE) { null }
        currentPlayer = Player.X
        gameState = GameState.Playing
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
            Text(strings.toolTitles["tictactoe"] ?: "Tic Tac Toe", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(24.dp))

        val statusText = when (val state = gameState) {
            is GameState.Playing -> {
                val playerName = when (currentPlayer) {
                    is Player.X -> "X"
                    is Player.O -> "O"
                }
                strings.gameTurn.format(playerName)
            }
            is GameState.Won -> {
                val winnerName = when (state.winner) {
                    is Player.X -> "X"
                    is Player.O -> "O"
                }
                strings.gameWins.format(winnerName)
            }
            is GameState.Draw -> strings.gameDraw
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        val winningCells = if (gameState is GameState.Won) (gameState as GameState.Won).line else emptyList()

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (row in 0 until BOARD_SIZE) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (col in 0 until BOARD_SIZE) {
                        val index = row * BOARD_SIZE + col
                        val cell = board[index]
                        val isWinning = index in winningCells
                        Cell(
                            text = when (cell) {
                                is Player.X -> "X"
                                is Player.O -> "O"
                                null -> ""
                            },
                            isWinning = isWinning,
                            onClick = { onCellClick(index) }
                        )
                    }
                }
            }
        }

        if (gameState !is GameState.Playing) {
        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(strings.gameBestScore.format(wins), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (rewardMsg != null) {
                Text(rewardMsg!!, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))
            Button(
                onClick = { haptic.performIfEnabled(); reset() },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.gamePlayAgain, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun Cell(
    text: String,
    isWinning: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (text.isNotEmpty()) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "cellScale",
    )
    val pulse by animateFloatAsState(
        targetValue = if (isWinning) 1.05f else 1f,
        animationSpec = tween(400),
        label = "cellPulse",
    )
    val bgColor = when {
        isWinning -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (text == "X") {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = Modifier
            .size(96.dp)
            .scale(scale * pulse)
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(
                width = if (isWinning) 2.dp else 0.dp,
                color = if (isWinning) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
