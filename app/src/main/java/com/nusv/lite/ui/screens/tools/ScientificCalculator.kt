package com.nusv.lite.ui.screens.tools

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import kotlin.math.*

private data class HistoryEntry(
    val expression: String,
    val result: String,
)

@Composable
fun ScientificCalculator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var useDegrees by remember { mutableStateOf(true) }
    var showHistory by remember { mutableStateOf(false) }
    var justEvaluated by remember { mutableStateOf(false) }
    val history = remember { mutableStateListOf<HistoryEntry>() }

    fun appendText(t: String) {
        if (justEvaluated) { expression = ""; result = ""; justEvaluated = false }
        expression += t
    }

    fun appendNum(n: String) {
        if (justEvaluated) { expression = ""; result = ""; justEvaluated = false }
        expression += n
    }

    fun appendOp(op: String) {
        if (justEvaluated) {
            if (result != "Error") expression = result
            result = ""
            justEvaluated = false
        }
        expression += op
    }

    fun appendFunc(name: String) {
        if (justEvaluated) { expression = ""; result = ""; justEvaluated = false }
        expression += "$name("
    }

    fun onEvaluate() {
        if (expression.isEmpty()) return
        val evalResult = evaluate(expression, useDegrees)
        if (evalResult != null) {
            val formatted = formatResult(evalResult)
            result = formatted
            history.add(0, HistoryEntry(expression, formatted))
            justEvaluated = true
        } else {
            result = "Error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp),
                    )
                    .clickable {
                        haptic.performIfEnabled()
                        onBack()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Scientific Calc",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp),
                    )
                    .clickable {
                        haptic.performIfEnabled()
                        showHistory = !showHistory
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    if (showHistory) "\u2715" else "\uD83D\uDCCB",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (result.isNotEmpty()) {
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp),
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            val shareText = "${expression.ifEmpty { "0" }} = $result"
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, null))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("\u2191", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = expression.ifEmpty { "0" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (useDegrees) "DEG" else "RAD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.clickable {
                            haptic.performIfEnabled()
                            useDegrees = !useDegrees
                        },
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = result.ifEmpty { "=" },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (showHistory) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(8.dp),
            ) {
                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No history",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(history) { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                        RoundedCornerShape(8.dp),
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = entry.expression,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "= ${entry.result}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("(", Category.PAREN, { appendText("(") }),
                        ButtonDef(")", Category.PAREN, { appendText(")") }),
                        ButtonDef(
                            if (expression.isEmpty()) "AC" else "C",
                            Category.CLEAR,
                            {
                                if (expression.isNotEmpty()) {
                                    expression = ""
                                    justEvaluated = false
                                } else {
                                    result = ""
                                }
                            },
                        ),
                        ButtonDef("\u232B", Category.CLEAR, {
                            if (justEvaluated) {
                                expression = ""
                                result = ""
                                justEvaluated = false
                            } else if (expression.isNotEmpty()) {
                                expression = expression.dropLast(1)
                            }
                        }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("sin", Category.FUNCTION, { appendFunc("sin") }),
                        ButtonDef("cos", Category.FUNCTION, { appendFunc("cos") }),
                        ButtonDef("tan", Category.FUNCTION, { appendFunc("tan") }),
                        ButtonDef("\u00F7", Category.OPERATOR, { appendOp("\u00F7") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("log", Category.FUNCTION, { appendFunc("log") }),
                        ButtonDef("ln", Category.FUNCTION, { appendFunc("ln") }),
                        ButtonDef("sqrt", Category.FUNCTION, { appendFunc("sqrt") }),
                        ButtonDef("\u00D7", Category.OPERATOR, { appendOp("\u00D7") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("7", Category.NUMBER, { appendNum("7") }),
                        ButtonDef("8", Category.NUMBER, { appendNum("8") }),
                        ButtonDef("9", Category.NUMBER, { appendNum("9") }),
                        ButtonDef("\u2212", Category.OPERATOR, { appendOp("\u2212") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("4", Category.NUMBER, { appendNum("4") }),
                        ButtonDef("5", Category.NUMBER, { appendNum("5") }),
                        ButtonDef("6", Category.NUMBER, { appendNum("6") }),
                        ButtonDef("+", Category.OPERATOR, { appendOp("+") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("1", Category.NUMBER, { appendNum("1") }),
                        ButtonDef("2", Category.NUMBER, { appendNum("2") }),
                        ButtonDef("3", Category.NUMBER, { appendNum("3") }),
                        ButtonDef("x\u00B2", Category.FUNCTION, { appendText("\u00B2") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef("0", Category.NUMBER, { appendNum("0") }),
                        ButtonDef(".", Category.NUMBER, { appendText(".") }),
                        ButtonDef("\u03C0", Category.CONSTANT, { appendText("\u03C0") }),
                        ButtonDef("x\u00B3", Category.FUNCTION, { appendText("\u00B3") }),
                    ),
                    haptic = haptic,
                )
                CalculatorButtonRow(
                    items = listOf(
                        ButtonDef(
                            "AC",
                            Category.CLEAR,
                            {
                                expression = ""
                                result = ""
                                justEvaluated = false
                            },
                        ),
                        ButtonDef("e", Category.CONSTANT, { appendText("e") }),
                        ButtonDef("1/x", Category.FUNCTION, { appendText("^(\u22121)") }),
                        ButtonDef("=", Category.EQUALS, { onEvaluate() }),
                    ),
                    haptic = haptic,
                )
            }
        }
    }
}

private data class ButtonDef(
    val text: String,
    val category: Category,
    val onClick: () -> Unit,
)

private enum class Category {
    NUMBER,
    OPERATOR,
    FUNCTION,
    CLEAR,
    PAREN,
    CONSTANT,
    SETTING,
    EQUALS,
}

@Composable
private fun CalculatorButtonRow(
    items: List<ButtonDef>,
    haptic: HapticFeedback,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        val colCount = maxOf(items.size, 4)
        for (item in items) {
            CalcButton(
                text = item.text,
                category = item.category,
                haptic = haptic,
                onClick = item.onClick,
                modifier = Modifier.weight(1f),
            )
        }
        if (items.size < colCount) {
            repeat(colCount - items.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CalcButton(
    text: String,
    category: Category,
    haptic: HapticFeedback,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = when (category) {
        Category.NUMBER -> MaterialTheme.colorScheme.surfaceVariant
        Category.OPERATOR -> MaterialTheme.colorScheme.primary
        Category.FUNCTION -> MaterialTheme.colorScheme.tertiary
        Category.CLEAR -> MaterialTheme.colorScheme.error
        Category.PAREN -> MaterialTheme.colorScheme.surfaceVariant
        Category.CONSTANT -> MaterialTheme.colorScheme.tertiary
        Category.SETTING -> MaterialTheme.colorScheme.surfaceVariant
        Category.EQUALS -> MaterialTheme.colorScheme.primary
    }
    val textColor = when (category) {
        Category.NUMBER -> MaterialTheme.colorScheme.onSurface
        Category.OPERATOR -> MaterialTheme.colorScheme.onPrimary
        Category.FUNCTION -> MaterialTheme.colorScheme.onTertiary
        Category.CLEAR -> MaterialTheme.colorScheme.onError
        Category.PAREN -> MaterialTheme.colorScheme.onSurface
        Category.CONSTANT -> MaterialTheme.colorScheme.onTertiary
        Category.SETTING -> MaterialTheme.colorScheme.onSurface
        Category.EQUALS -> MaterialTheme.colorScheme.onPrimary
    }
    val fontSize = when {
        text.length > 3 -> 14.sp
        text.length > 2 -> 15.sp
        else -> 17.sp
    }

    Box(
        modifier = modifier
            .height(52.dp)
            .background(bgColor, RoundedCornerShape(12.dp))
            .clickable {
                haptic.performIfEnabled()
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = textColor,
            ),
            textAlign = TextAlign.Center,
        )
    }
}

private fun evaluate(expression: String, deg: Boolean): Double? {
    return try {
        val prepared = expression
            .replace("\u00D7", "*")
            .replace("\u00F7", "/")
            .replace("\u00B2", "^2")
            .replace("\u00B3", "^3")
            .replace("\u03C0", "pi")
        val tokens = tokenize(prepared)
        val postfix = infixToPostfix(tokens)
        evalPostfix(postfix, deg)
    } catch (_: Exception) { null }
}

private fun tokenize(expr: String): List<String> {
    val result = mutableListOf<String>()
    var i = 0
    val s = expr.replace("\u2212", "-")
    while (i < s.length) {
        when {
            s[i].isWhitespace() -> i++
            s[i].isDigit() || s[i] == '.' -> {
                val sb = StringBuilder()
                while (i < s.length && (s[i].isDigit() || s[i] == '.')) { sb.append(s[i]); i++ }
                result.add(sb.toString())
            }
            s[i] == '-' && (i == 0 || s[i - 1] in "(*/+-^\u2212") -> {
                result.add("_"); i++
            }
            s[i].isLetter() -> {
                val sb = StringBuilder()
                while (i < s.length && s[i].isLetter()) { sb.append(s[i]); i++ }
                result.add(sb.toString())
            }
            else -> {
                result.add(s[i].toString()); i++
            }
        }
    }
    return result
}

private fun infixToPostfix(tokens: List<String>): List<String> {
    val output = mutableListOf<String>()
    val stack = mutableListOf<String>()

    for (token in tokens) {
        when {
            token.first().isDigit() || (token.length > 1 && token.first() == '-' && token[1].isDigit()) -> {
                output.add(token)
            }
            token == "pi" || token == "e" -> {
                output.add(token)
            }
            token in functionNames -> {
                stack.add(token)
            }
            token == "(" -> {
                stack.add(token)
            }
            token == ")" -> {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    output.add(stack.removeAt(stack.size - 1))
                }
                if (stack.isNotEmpty() && stack.last() == "(") {
                    stack.removeAt(stack.size - 1)
                }
                if (stack.isNotEmpty() && stack.last() in functionNames) {
                    output.add(stack.removeAt(stack.size - 1))
                }
            }
            token in operatorNames -> {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    val top = stack.last()
                    if (top in operatorNames || top in functionNames) {
                        val precTop = precedence(top)
                        val precTok = precedence(token)
                        if (precTop > precTok || (precTop == precTok && !isRightAssoc(token))) {
                            output.add(stack.removeAt(stack.size - 1))
                        } else break
                    } else break
                }
                stack.add(token)
            }
            token == "_" -> {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    val top = stack.last()
                    if (top in operatorNames || top in functionNames) {
                        val precTop = precedence(top)
                        val precTok = precedence("_")
                        if (precTop > precTok || (precTop == precTok && !isRightAssoc("_"))) {
                            output.add(stack.removeAt(stack.size - 1))
                        } else break
                    } else break
                }
                stack.add("_")
            }
        }
    }

    while (stack.isNotEmpty()) {
        val op = stack.removeAt(stack.size - 1)
        if (op != "(") output.add(op)
    }

    return output
}

private fun evalPostfix(postfix: List<String>, deg: Boolean): Double {
    val stack = mutableListOf<Double>()

    for (token in postfix) {
        when {
            token.first().isDigit() || (token.length > 1 && token.first() == '-') -> {
                stack.add(token.toDouble())
            }
            token == "pi" -> stack.add(Math.PI)
            token == "e" -> stack.add(Math.E)
            token in functionNames -> {
                val arg = stack.removeAt(stack.size - 1)
                val result = when (token) {
                    "sin" -> {
                        val a = if (deg) Math.toRadians(arg) else arg
                        sin(a)
                    }
                    "cos" -> {
                        val a = if (deg) Math.toRadians(arg) else arg
                        cos(a)
                    }
                    "tan" -> {
                        val a = if (deg) Math.toRadians(arg) else arg
                        tan(a)
                    }
                    "log" -> log10(arg)
                    "ln" -> ln(arg)
                    "sqrt" -> sqrt(arg)
                    else -> 0.0
                }
                stack.add(result)
            }
            token == "_" -> {
                val arg = stack.removeAt(stack.size - 1)
                stack.add(-arg)
            }
            token in operatorNames -> {
                val b = stack.removeAt(stack.size - 1)
                val a = stack.removeAt(stack.size - 1)
                val result = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> a / b
                    "^" -> a.pow(b)
                    else -> 0.0
                }
                stack.add(result)
            }
        }
    }

    return stack.last()
}

private val functionNames = setOf("sin", "cos", "tan", "log", "ln", "sqrt")
private val operatorNames = setOf("+", "-", "*", "/", "^")

private fun precedence(op: String): Int = when (op) {
    "+", "-" -> 1
    "*", "/" -> 2
    "^", "_" -> 3
    else -> 0
}

private fun isRightAssoc(op: String): Boolean = op == "^" || op == "_"

private fun formatResult(d: Double): String {
    return if (d.isNaN() || d.isInfinite()) {
        "Error"
    } else if (d == floor(d) && !d.isInfinite()) {
        if (d >= 1e15 || d <= -1e15) {
            String.format("%.6e", d)
        } else {
            d.toLong().toString()
        }
    } else {
        String.format("%.10f", d).trimEnd('0').trimEnd('.')
    }
}
