package com.nusv.lite.ui.screens.tools

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled

enum class ViewMode { Edit, Preview, Split }

@Composable
fun MarkdownPreview(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val sample = """
# Markdown Preview

## Features

- **Bold** and *italic* text
- `Inline code` and code blocks
- Lists and headings

## Code

```
val x = 42
println("Hello")
```

> Blockquote example
    """.trimIndent()

    var text by remember { mutableStateOf(TextFieldValue(sample)) }
    var mode by remember { mutableStateOf(ViewMode.Split) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
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
            Text("Markdown Preview", style = MaterialTheme.typography.headlineMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ViewMode.entries.forEach { entry ->
                val selected = mode == entry
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { mode = entry }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        entry.name,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        AnimatedContent(
            targetState = mode,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "modeTransition"
        ) { currentMode ->
            Column(modifier = Modifier.fillMaxSize()) {
                if (currentMode == ViewMode.Edit || currentMode == ViewMode.Split) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        BasicTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (text.text.isEmpty()) {
                                        Text(
                                            "Enter Markdown...",
                                            style = TextStyle(
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 14.sp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                .copy(alpha = 0.5f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
                if (currentMode == ViewMode.Split) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
                if (currentMode == ViewMode.Preview || currentMode == ViewMode.Split) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        MarkdownText(text.text)
                    }
                }
            }
        }
    }
}

@Composable
fun MarkdownText(markdown: String) {
    val lines = markdown.split("\n")
    var inCodeBlock = false
    var codeContent = ""
    Column {
        lines.forEach { line ->
            when {
                line.startsWith("```") -> {
                    if (inCodeBlock) {
                        Text(
                            codeContent.trimEnd(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        )
                        codeContent = ""
                    }
                    inCodeBlock = !inCodeBlock
                }
                inCodeBlock -> codeContent += line + "\n"
                line.startsWith("# ") -> Text(
                    line.removePrefix("# "),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                line.startsWith("## ") -> Text(
                    line.removePrefix("## "),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                line.startsWith("### ") -> Text(
                    line.removePrefix("### "),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                line.startsWith("> ") -> Text(
                    line.removePrefix("> "),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp)
                )
                line.startsWith("- ") -> Text(
                    "  \u2022  ${line.removePrefix("- ")}",
                    style = MaterialTheme.typography.bodyLarge
                )
                line.matches(Regex("\\d+\\.\\s.*")) -> {
                    val num = line.substringBefore(".")
                    val body = line.substringAfter(" ")
                    Text("  $num. $body", style = MaterialTheme.typography.bodyLarge)
                }
                line == "---" -> HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                line.isBlank() -> Spacer(Modifier.height(8.dp))
                else -> RichText(line)
            }
        }
    }
}

@Composable
fun RichText(line: String) {
    val uriHandler = LocalUriHandler.current
    val annotated = buildAnnotatedString {
        var remaining = line
        while (remaining.isNotEmpty()) {
            when {
                remaining.startsWith("**") -> {
                    val end = remaining.indexOf("**", 2)
                    if (end > 0) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(remaining.substring(2, end))
                        }
                        remaining = remaining.substring(end + 2)
                    } else {
                        append(remaining)
                        remaining = ""
                    }
                }
                remaining.startsWith("*") && !remaining.startsWith("**") -> {
                    val end = remaining.indexOf("*", 1)
                    if (end > 0) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(remaining.substring(1, end))
                        }
                        remaining = remaining.substring(end + 1)
                    } else {
                        append(remaining)
                        remaining = ""
                    }
                }
                remaining.startsWith("`") && !remaining.startsWith("``") -> {
                    val end = remaining.indexOf("`", 1)
                    if (end > 0) {
                        withStyle(
                            SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                background = Color.LightGray.copy(alpha = 0.3f)
                            )
                        ) {
                            append(remaining.substring(1, end))
                        }
                        remaining = remaining.substring(end + 1)
                    } else {
                        append(remaining)
                        remaining = ""
                    }
                }
                remaining.startsWith("[") -> {
                    val closeBracket = remaining.indexOf("]")
                    val openParen = remaining.indexOf("(", closeBracket)
                    val closeParen = remaining.indexOf(")", openParen)
                    if (closeBracket > 0 && openParen == closeBracket + 1 && closeParen > openParen) {
                        val linkText = remaining.substring(1, closeBracket)
                        val url = remaining.substring(openParen + 1, closeParen)
                        pushStringAnnotation("URL", url)
                        withStyle(
                            SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(linkText)
                        }
                        pop()
                        remaining = remaining.substring(closeParen + 1)
                    } else {
                        append(remaining.first())
                        remaining = remaining.substring(1)
                    }
                }
                else -> {
                    val markers = listOf("**", "*", "`", "[")
                    var nextIndex = Int.MAX_VALUE
                    for (m in markers) {
                        val idx = remaining.indexOf(m)
                        if (idx >= 0 && idx < nextIndex) nextIndex = idx
                    }
                    if (nextIndex > 0 && nextIndex < Int.MAX_VALUE) {
                        append(remaining.substring(0, nextIndex))
                        remaining = remaining.substring(nextIndex)
                    } else {
                        append(remaining)
                        remaining = ""
                    }
                }
            }
        }
    }
    ClickableText(
        text = annotated,
        style = MaterialTheme.typography.bodyLarge,
        onClick = { offset ->
            annotated.getStringAnnotations("URL", offset, offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        }
    )
}
