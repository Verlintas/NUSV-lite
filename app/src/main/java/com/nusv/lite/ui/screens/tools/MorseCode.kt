package com.nusv.lite.ui.screens.tools

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val textToMorse = mapOf(
    'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
    'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
    'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
    'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
    'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
    'Z' to "--..",
    '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
    '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----.",
    '.' to ".-.-.-", ',' to "--..--", '?' to "..--..", '!' to "-.-.--",
    ':' to "---...", ';' to "-.-.-.", '\'' to ".----.", '"' to ".-..-.",
    '/' to "-..-.", '@' to ".--.-.",
)

private val morseToText = textToMorse.entries.associate { (k, v) -> v to k.toString() }

fun textToMorseCode(text: String): String {
    return text.uppercase().map { c ->
        when {
            c == ' ' -> "/"
            else -> textToMorse[c] ?: c.toString()
        }
    }.joinToString(" ")
}

fun morseToTextCode(morse: String): String {
    return morse.split(" ").map { code ->
        when (code) {
            "/" -> " "
            else -> morseToText[code] ?: code
        }
    }.joinToString("")
}

private fun vibrate(vibrator: Vibrator, ms: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(ms)
    }
}

private fun playMorse(morse: String, vibrator: Vibrator, scope: kotlinx.coroutines.CoroutineScope) {
    scope.launch {
        for (char in morse) {
            when (char) {
                '.' -> { vibrate(vibrator, 50); delay(150) }
                '-' -> { vibrate(vibrator, 200); delay(300) }
                ' ' -> delay(300)
            }
        }
    }
}

@Composable
fun MorseCode(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    var isTextToMorse by remember { mutableStateOf(true) }
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() },
                contentAlignment = Alignment.Center,
            ) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Text("Morse Code", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Text \u2192 Morse", "Morse \u2192 Text").forEachIndexed { index, label ->
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if ((isTextToMorse && index == 0) || (!isTextToMorse && index == 1))
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            isTextToMorse = index == 0
                            input = ""
                            output = ""
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if ((isTextToMorse && index == 0) || (!isTextToMorse && index == 1))
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
        ) {
            BasicTextField(
                value = input,
                onValueChange = {
                    input = it
                    output = if (isTextToMorse) textToMorseCode(it) else morseToTextCode(it)
                },
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (input.isEmpty()) {
                        Text(
                            if (isTextToMorse) "Enter text..." else "Enter Morse code...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                        )
                    }
                    innerTextField()
                },
            )
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(12.dp),
        ) {
            Text(
                text = output,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        haptic.performIfEnabled()
                        clipboard.setText(AnnotatedString(output))
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "\uD83D\uDCCB Copy",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            if (isTextToMorse && output.isNotBlank()) {
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            haptic.performIfEnabled()
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            playMorse(output, vibrator, scope)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "\u25B6 Play",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
