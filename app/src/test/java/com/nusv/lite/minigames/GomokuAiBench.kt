package com.nusv.lite.minigames

import org.junit.Test
import kotlin.system.measureNanoTime

class GomokuAiBench {

    @Test
    fun benchmark() {
        println("warmup")
        val w = MutableList(225) { 0 }
        w[IndexOf(7, 7)] = 1
        findBestMove(w, 2, 1)

        val b1 = MutableList(225) { 0 }
        b1[IndexOf(7, 7)] = 1
        play("move2", b1)

        val b2 = MutableList(225) { 0 }
        val m2 = listOf(7 to 7, 7 to 8, 8 to 7, 6 to 8, 6 to 6, 7 to 5, 8 to 6, 5 to 7, 9 to 5, 5 to 6, 4 to 7, 6 to 9)
        m2.forEachIndexed { i, (r, c) -> b2[IndexOf(r, c)] = if (i % 2 == 0) 1 else 2 }
        play("midgame12", b2)

        val b4 = MutableList(225) { 0 }
        val m4 = listOf(
            7 to 7, 7 to 8, 8 to 7, 6 to 8, 6 to 6, 7 to 5, 8 to 6, 5 to 7, 9 to 5, 5 to 6,
            4 to 7, 6 to 9, 4 to 6, 6 to 10, 3 to 5, 7 to 11, 3 to 6, 8 to 10, 2 to 5, 9 to 12,
            3 to 7, 8 to 11, 4 to 4, 5 to 9, 3 to 4, 6 to 11, 2 to 4, 7 to 12, 2 to 6, 8 to 9,
            5 to 5, 9 to 6
        )
        m4.forEachIndexed { i, (r, c) -> b4[IndexOf(r, c)] = if (i % 2 == 0) 1 else 2 }
        play("dense30", b4)

        val random = kotlin.random.Random(42)
        var b = MutableList(225) { 0 }
        b[IndexOf(7, 7)] = 2
        repeat(8) { turn ->
            val idx = findBestMove(b, 1, 2)
            val t = measureNanoTime { findBestMove(b, 1, 2) }
            b[idx] = 1
            println("simAI%-3d %5dms -> %d,%d".format(turn, t / 1_000_000, idx / 15, idx % 15))
            if (checkWin(b, idx)) {
                println("AI won in $turn")
                return
            }
            val empty = (0 until 225).filter { b[it] == 0 }
            b[empty.random(random)] = 2
            if (turn > 5) {
                println("player created open-three scenario, AI to respond")
                play("urgent", b)
                return
            }
        }
    }

    @Test
    fun deltaConsistency() {
        val random = kotlin.random.Random(7)
        var board = MutableList(225) { 0 }
        board[IndexOf(7, 7)] = 1
        var total = evaluateBoard(board, 2, 1)
        var i = 0
        while (i < 20) {
            val mover = if (i % 2 == 0) 2 else 1
            val empty = (0 until 225).filter { board[it] == 0 }
            val idx = empty.random(random)
            board[idx] = mover
            total += deltaEval(board, idx, mover, 2) + deltaEval(board, idx, if (mover == 1) 2 else 1, 2)
            val full = evaluateBoard(board, 2, 1)
            if (total != full) {
                val sb = StringBuilder()
                for (rr in 0 until 15) {
                    for (cc in 0 until 15) sb.append(if (board[IndexOf(rr, cc)] == 0) '.' else board[IndexOf(rr, cc)].toString())
                    sb.append('\n')
                }
                throw AssertionError("mismatch at move $i mover=$mover idx=$idx\nincremental=$total full=$full\n" + sb)
            }
            if (checkWin(board, idx)) break
            i++
        }
        println("delta consistency OK")
    }

    @Test
    fun ultimateVsBeginner() {
        var w = 0
        var l = 0
        var d = 0
        repeat(6) {
            val b = MutableList(225) { 0 }
            b[IndexOf(7, 7)] = 1
            var turn = 1
            while (true) {
                val idx = if (turn % 2 == 0) findBestMove(b, 1, 2) else findEasyMove(b, 2, 1)
                if (idx < 0) {
                    d++
                    break
                }
                b[idx] = if (turn % 2 == 0) 1 else 2
                if (checkWin(b, idx)) {
                    println("game $turn moves, winner=${if (turn % 2 == 0) "ultimate" else "beginner"}")
                    if (turn % 2 == 0) w++ else l++
                    break
                }
                turn++
                if (turn > 220) {
                    d++
                    break
                }
            }
        }
        println("ultimate(1st) vs beginner: W$w L$l D$d")
    }

    private fun play(name: String, b: MutableList<Int>) {
        val idx = findBestMove(b, 2, 1)
        val t = measureNanoTime { findBestMove(b, 2, 1) }
        println("%-10s %5dms -> %d,%d".format(name, t / 1_000_000, idx / 15, idx % 15))
    }
}

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

private fun canWinAt(board: List<Int>, idx: Int, player: Int): Boolean {
    val r = idx / GOMOKU_SIZE
    val c = idx % GOMOKU_SIZE
    for ((dr, dc) in listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)) {
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

private fun winMoves(board: List<Int>, player: Int, empties: List<Int>): List<Int> {
    val res = mutableListOf<Int>()
    for (idx in empties) {
        if (canWinAt(board, idx, player)) res.add(idx)
    }
    return res
}

private fun isOpenFourAt(board: List<Int>, idx: Int, player: Int): Boolean {
    val r = idx / GOMOKU_SIZE
    val c = idx % GOMOKU_SIZE
    for ((dr, dc) in listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)) {
        var count = 1
        var f = 1
        while (f <= 4) {
            val nr = r + dr * f
            val nc = c + dc * f
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) {
                count++
                f++
            } else break
        }
        var b = 1
        while (b <= 4) {
            val nr = r - dr * b
            val nc = c - dc * b
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == player) {
                count++
                b++
            } else break
        }
        if (count == 4) {
            val fr = r + dr * f
            val fc = c + dc * f
            val br = r - dr * b
            val bc = c - dc * b
            if (fr in 0 until GOMOKU_SIZE && fc in 0 until GOMOKU_SIZE &&
                br in 0 until GOMOKU_SIZE && bc in 0 until GOMOKU_SIZE &&
                board[IndexOf(fr, fc)] == 0 && board[IndexOf(br, bc)] == 0
            ) return true
        }
    }
    return false
}

private fun fourMoves(board: List<Int>, player: Int, empties: List<Int>): List<Int> {
    val res = mutableListOf<Int>()
    for (idx in empties) {
        if (isOpenFourAt(board, idx, player)) res.add(idx)
    }
    return res
}

private fun bestBlock(board: List<Int>, other: Int, threats: List<Int>): Int {
    var best = threats.first()
    var bestRemaining = Int.MAX_VALUE
    for (idx in threats.distinct()) {
        val nb = board.toMutableList()
        nb[idx] = 3 - other
        var remaining = 0
        for (t in threats) {
            if (t == idx || nb[t] != 0) continue
            if (isOpenFourAt(nb, t, other)) remaining++
        }
        if (remaining < bestRemaining) {
            bestRemaining = remaining
            best = idx
        }
    }
    return best
}

private fun urgentMove(board: List<Int>, mover: Int, other: Int): Int? {
    val empties = nearCandidates(board)
    winMoves(board, mover, empties).firstOrNull()?.let { return it }
    winMoves(board, other, empties).firstOrNull()?.let { return it }
    fourMoves(board, mover, empties).firstOrNull()?.let { return it }
    val otherFours = fourMoves(board, other, empties)
    if (otherFours.isNotEmpty()) return bestBlock(board, other, otherFours)
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

private fun deltaEval(board: List<Int>, idx: Int, p: Int, me: Int): Long {
    val r = idx / GOMOKU_SIZE
    val c = idx % GOMOKU_SIZE
    var total = 0L
    val dirs = listOf(1 to 0, 0 to 1, 1 to 1, 1 to -1)
    val ownStone = board[idx] == p
    for ((dr, dc) in dirs) {
        var s = 0L
        if (ownStone) {
            var count = 1
            var opens = 0
            var f = 1
            while (true) {
                val nr = r + dr * f
                val nc = c + dc * f
                if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == p) {
                    count++
                    f++
                } else {
                    if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) opens++
                    break
                }
            }
            var b = 1
            while (true) {
                val nr = r - dr * b
                val nc = c - dc * b
                if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == p) {
                    count++
                    b++
                } else {
                    if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) opens++
                    break
                }
            }
            s += lineScore(count, opens).toLong()
        }
        var f2 = 1
        var count2 = 0
        var opens2 = 0
        while (true) {
            val nr = r + dr * f2
            val nc = c + dc * f2
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == p) {
                count2++
                f2++
            } else break
        }
        if (count2 >= 1) {
            val nr = r + dr * f2
            val nc = c + dc * f2
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) opens2++
            val oldScore = lineScore(count2, opens2 + 1).toLong()
            val newScore = if (ownStone) 0L else lineScore(count2, opens2).toLong()
            s += newScore - oldScore
        }
        var b2 = 1
        var count3 = 0
        var opens3 = 0
        while (true) {
            val nr = r - dr * b2
            val nc = c - dc * b2
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == p) {
                count3++
                b2++
            } else break
        }
        if (count3 >= 1) {
            val nr = r - dr * b2
            val nc = c - dc * b2
            if (nr in 0 until GOMOKU_SIZE && nc in 0 until GOMOKU_SIZE && board[IndexOf(nr, nc)] == 0) opens3++
            val oldScore = lineScore(count3, opens3 + 1).toLong()
            val newScore = if (ownStone) 0L else lineScore(count3, opens3).toLong()
            s += newScore - oldScore
        }
        total += s
    }
    return if (p == me) total else -total
}

private fun nearCandidates(board: List<Int>, exclude: Int = -1): List<Int> {
    val empty = mutableListOf<Int>()
    for (r in 0 until GOMOKU_SIZE) {
        for (c in 0 until GOMOKU_SIZE) {
            val idx = IndexOf(r, c)
            if (board[idx] != 0) continue
            if (idx == exclude) continue
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
    return empty
}

private fun topCandidates(board: List<Int>, player: Int, other: Int, n: Int, exclude: Int = -1): List<Int> =
    nearCandidates(board, exclude).sortedByDescending { idx ->
        scorePoint(board, idx, player).toLong() + scorePoint(board, idx, other)
    }.take(n)

private fun searchMove(
    board: List<Int>,
    me: Int,
    opponent: Int,
    mover: Int,
    depth: Int,
    branch: Int,
    totalEval: Long,
    alpha: Long,
    beta: Long,
    deadline: Long,
): Long {
    val other = if (mover == me) opponent else me
    if (depth == 0) {
        val forced = urgentMove(board, other, mover)
        if (forced != null) {
            val nb = board.toMutableList()
            nb[forced] = other
            val total = totalEval + deltaEval(nb, forced, other, me) + deltaEval(nb, forced, mover, me)
            return searchMove(nb, me, opponent, mover, 3, 1, total, alpha, beta, deadline)
        }
        return totalEval
    }
    val forced = urgentMove(board, mover, other)
    val candidates = if (forced != null) listOf(forced) else topCandidates(board, mover, other, branch)
    if (candidates.isEmpty()) return totalEval
    if (System.nanoTime() > deadline) return totalEval
    if (mover == me) {
        var best = Long.MIN_VALUE
        var a = alpha
        for (idx in candidates) {
            val nb = board.toMutableList()
            nb[idx] = mover
            val total = totalEval + deltaEval(nb, idx, mover, me) + deltaEval(nb, idx, other, me)
            val s = searchMove(nb, me, opponent, other, depth - 1, branch, total, a, beta, deadline)
            if (s > best) best = s
            if (best > a) a = best
            if (a >= beta) break
        }
        return best
    } else {
        var worst = Long.MAX_VALUE
        var b = beta
        for (idx in candidates) {
            val nb = board.toMutableList()
            nb[idx] = mover
            val total = totalEval + deltaEval(nb, idx, mover, me) + deltaEval(nb, idx, other, me)
            val s = searchMove(nb, me, opponent, other, depth - 1, branch, total, alpha, b, deadline)
            if (s < worst) worst = s
            if (worst < b) b = worst
            if (b <= alpha) break
        }
        return worst
    }
}

private fun findBestMove(board: List<Int>, me: Int, opponent: Int): Int {
    val empty = nearCandidates(board)
    if (empty.isEmpty()) return -1
    if (empty.size == GOMOKU_SIZE * GOMOKU_SIZE) return IndexOf(7, 7)

    urgentMove(board, me, opponent)?.let { return it }

    val ordered = empty.sortedByDescending { idx ->
        scorePoint(board, idx, me).toLong() + scorePoint(board, idx, opponent)
    }
    val initEval = evaluateBoard(board, me, opponent)
    val deadline = System.nanoTime() + 120_000_000_000L
    var bestIdx = ordered.first()
    var bestScore = Long.MIN_VALUE
    for (idx in ordered) {
        val nb = board.toMutableList()
        nb[idx] = me
        val total = initEval + deltaEval(nb, idx, me, me) + deltaEval(nb, idx, opponent, me)
        val s = searchMove(nb, me, opponent, opponent, 7, 5, total, Long.MIN_VALUE, Long.MAX_VALUE, deadline)
        if (s > bestScore) {
            bestScore = s
            bestIdx = idx
        }
        if (System.nanoTime() > deadline) break
    }
    return bestIdx
}

private fun findEasyMove(board: List<Int>, me: Int, opponent: Int): Int {
    val empty = nearCandidates(board)
    if (empty.isEmpty()) return -1
    if (empty.size == GOMOKU_SIZE * GOMOKU_SIZE) return IndexOf(7, 7)

    winMoves(board, me, empty).firstOrNull()?.let { return it }
    winMoves(board, opponent, empty).firstOrNull()?.let { return it }

    var bestIdx = empty.first()
    var bestScore = Long.MIN_VALUE
    for (idx in empty) {
        val attack = scorePoint(board, idx, me).toLong()
        val defense = scorePoint(board, idx, opponent).toLong() / 2
        val total = attack + defense + kotlin.random.Random.nextLong(-500, 500)
        if (total > bestScore) {
            bestScore = total
            bestIdx = idx
        }
    }
    return bestIdx
}

