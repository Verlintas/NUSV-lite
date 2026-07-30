package com.nusv.lite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val bg = MaterialTheme.colorScheme.background
    val isOrca = bg == Color.Black && MaterialTheme.colorScheme.onBackground == Color.White
    val isDark = bg == Color(0xFF000000) || isOrca
    val cardBg = if (isOrca) Color.Black else (if (isDark) Color.White.copy(alpha = 0.06f) else Color.White)
    val cardBorder = if (isOrca) Color.White.copy(alpha = 0.5f) else (if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f))

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg, RoundedCornerShape(12.dp))
            .border(if (isOrca) 1.dp else 0.5.dp, cardBorder, RoundedCornerShape(12.dp)),
        content = content
    )
}
