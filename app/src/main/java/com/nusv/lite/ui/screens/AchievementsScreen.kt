package com.nusv.lite.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.Achievement
import com.nusv.lite.util.AchievementManager
import com.nusv.lite.util.LanguagePrefs
import com.nusv.lite.util.LocalAppStrings

@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    var unlockedCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        AchievementManager.checkAndUnlock(ctx)
        unlockedCount = AchievementManager.unlockedCount(ctx)
    }

    val isEn = LanguagePrefs.get().code == "en"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBack() }) { Text("‹") }
            Column {
                Text(strings.achievementsTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = if (isEn) "$unlockedCount / ${AchievementManager.ALL.size} unlocked"
                    else "已解锁 $unlockedCount / ${AchievementManager.ALL.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(AchievementManager.ALL) { ach ->
                AchievementCard(ach, unlocked = AchievementManager.isUnlocked(ctx, ach.id), isEn = isEn)
            }
        }
    }
}

@Composable
private fun AchievementCard(ach: Achievement, unlocked: Boolean, isEn: Boolean) {
    val name = if (isEn) ach.enName else ach.zhName
    val desc = if (isEn) ach.enDesc else ach.zhDesc
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (unlocked) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = ach.icon,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.alpha(if (unlocked) 1f else 0.35f)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (unlocked) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = desc,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = if (unlocked) "✓" else "🔒",
            style = MaterialTheme.typography.labelLarge,
            color = if (unlocked) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
