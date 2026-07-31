package com.nusv.lite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nusv.lite.data.SyncManager
import com.nusv.lite.model.Category
import com.nusv.lite.model.Item
import com.nusv.lite.repository.AppRepository
import com.nusv.lite.ui.components.NusvChip
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.ClickTracker
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled
import com.nusv.lite.util.scalePress
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: AppRepository,
    syncManager: SyncManager,
    onItemClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToolClick: (String) -> Unit = {},
) {
    val categories by repository.allCategories.collectAsState(initial = emptyList())
    val featuredItems by repository.featuredItems.collectAsState(initial = emptyList())
    val allItems by repository.allItems.collectAsState(initial = emptyList())
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current
    val recentToolIds = remember { mutableStateOf(ClickTracker.getRecent(ctx)) }
    LaunchedEffect(Unit) {
        recentToolIds.value = ClickTracker.getRecent(ctx)
    }
    val recentApps = remember(recentToolIds.value) {
        miniApps.filter { it.id in recentToolIds.value }
            .sortedByDescending { recentToolIds.value.indexOf(it.id) }
    }

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            haptic.performIfEnabled()
            scope.launch {
                syncManager.syncAll()
                refreshing = false
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
        item {
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (PointsManager.isOrcaActive(ctx)) "NUSV Orca" else "NUSV",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "V",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        item {
            val isShOrca = MaterialTheme.colorScheme.background == Color.Black &&
                MaterialTheme.colorScheme.onBackground == Color.White
            val shBw = if (isShOrca) 1.dp else 0.dp
            val shBc = if (isShOrca) Color.White.copy(alpha = 0.5f) else Color.Transparent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(shBw, shBc, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); onSearchClick() },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = strings.cdSearch,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = strings.homeSearch,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(28.dp))
        }

        item {
            Text(
                text = strings.homeFeatured,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(featuredItems) { item ->
                    FeaturedCard(
                        item = item,
                        category = categories.find { it.id == item.categoryId },
                        onClick = { onItemClick(item.id) },
                        modifier = Modifier.width(300.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        if (recentApps.isNotEmpty()) {
            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = strings.recentTools,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recentApps) { app ->
                        val isRtOrca = MaterialTheme.colorScheme.background == Color.Black &&
                            MaterialTheme.colorScheme.onBackground == Color.White
                        val rtBw = if (isRtOrca) 1.dp else 0.dp
                        val rtBc = if (isRtOrca) Color.White.copy(alpha = 0.5f) else Color.Transparent
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(rtBw, rtBc, RoundedCornerShape(12.dp))
                                .clickable {
                                    haptic.performIfEnabled()
                                    ClickTracker.increment(ctx, app.id)
                                    _pendingToolId = app.id
                                    onToolClick(app.id)
                                }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = app.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = app.title.take(12),
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        items(allItems) { item ->
            ItemRow(
                item = item,
                strings = strings,
                onClick = { onItemClick(item.id) }
            )
        }
    }
    }
}

@Composable
fun FeaturedCard(
    item: Item,
    category: Category?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val categoryColor = Color(category?.color ?: 0xFFFF2D78)
    val isDark = isSystemInDarkTheme()
    val isOrca = MaterialTheme.colorScheme.background == Color.Black &&
        MaterialTheme.colorScheme.onBackground == Color.White

    Box(
        modifier = modifier
            .height(180.dp)
            .scalePress(interactionSource)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isOrca) Brush.linearGradient(
                    colors = listOf(Color.Black, Color.Black),
                    start = Offset.Zero,
                    end = Offset(1000f, 1000f)
                ) else if (isDark) Brush.linearGradient(
                    colors = listOf(
                        categoryColor.copy(alpha = 0.8f),
                        categoryColor.copy(alpha = 0.3f),
                        Color(0xFF0A0A0A)
                    ),
                    start = Offset.Zero,
                    end = Offset(1000f, 1000f)
                ) else Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7C4DFF),
                        Color(0xFFB388FF),
                    ),
                    start = Offset.Zero,
                    end = Offset(1000f, 1000f)
                )
            )
            .let { mod -> if (isOrca) mod.border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)) else mod }
            .clickable(interactionSource = interactionSource, indication = null) { haptic.performIfEnabled(); onClick() }
            .padding(20.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            if (category != null) {
                NusvChip(text = category.name, selected = true)
                Spacer(Modifier.height(8.dp))
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ItemRow(
    item: Item,
    strings: com.nusv.lite.util.AppStrings,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { haptic.performIfEnabled(); onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = formatTimeAgo(item.createdAt, strings),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
    }
}

private fun formatTimeAgo(epochMillis: Long, strings: com.nusv.lite.util.AppStrings): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMillis
    return when {
        diff < 60_000 -> strings.timeJustNow
        diff < 3600_000 -> strings.timeMinAgo.format(diff / 60_000)
        diff < 86_400_000 -> strings.timeHourAgo.format(diff / 3600_000)
        diff < 604_800_000 -> strings.timeDayAgo.format(diff / 86_400_000)
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(epochMillis))
    }
}
