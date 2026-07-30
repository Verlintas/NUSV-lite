package com.nusv.lite.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.nusv.lite.repository.AppRepository
import com.nusv.lite.ui.components.NusvChip
import com.nusv.lite.util.performIfEnabled

@Composable
fun BrowseScreen(
    repository: AppRepository,
    onItemClick: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val categories by repository.allCategories.collectAsState(initial = emptyList())
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    val items by if (selectedCategoryId == null) {
        repository.allItems.collectAsState(initial = emptyList())
    } else {
        repository.getItemsByCategory(selectedCategoryId!!).collectAsState(initial = emptyList())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        item {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Browse",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    NusvChip(
                        text = "All",
                        selected = selectedCategoryId == null,
                        onClick = { haptic.performIfEnabled(); selectedCategoryId = null }
                    )
                }
                items(categories) { category ->
                    NusvChip(
                        text = category.name,
                        selected = selectedCategoryId == category.id,
                        onClick = { haptic.performIfEnabled(); selectedCategoryId = category.id }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        items(items) { item ->
            ItemRow(
                item = item,
                onClick = { haptic.performIfEnabled(); onItemClick(item.id) }
            )
        }
    }
}
