package com.nusv.lite.ui.screens.tools

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.SoundManager
import com.nusv.lite.util.performIfEnabled
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private data class AnniversaryEvent(
    val name: String,
    val dateMillis: Long,
)

private fun loadEvents(c: Context): MutableList<AnniversaryEvent> {
    val prefs = c.getSharedPreferences("anniversary_prefs", Context.MODE_PRIVATE)
    val count = prefs.getInt("count", 0)
    val list = mutableListOf<AnniversaryEvent>()
    for (i in 0 until count) {
        val name = prefs.getString("name_$i", "") ?: ""
        val millis = prefs.getLong("date_$i", 0L)
        if (name.isNotEmpty() && millis > 0) list.add(AnniversaryEvent(name, millis))
    }
    return list
}

private fun saveEvents(c: Context, events: List<AnniversaryEvent>) {
    val prefs = c.getSharedPreferences("anniversary_prefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putInt("count", events.size)
    events.forEachIndexed { i, e ->
        editor.putString("name_$i", e.name)
        editor.putLong("date_$i", e.dateMillis)
    }
    editor.apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anniversary(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val ctx = LocalContext.current

    var events by remember { mutableStateOf<List<AnniversaryEvent>>(loadEvents(ctx)) }
    var showAdd by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var showPicker by remember { mutableStateOf(false) }
    var pickedMillis by remember { mutableStateOf<Long?>(null) }

    fun daysBetween(millis: Long): Long {
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = millis
        }
        val date = LocalDate.of(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH))
        return ChronoUnit.DAYS.between(date, LocalDate.now())
    }

    fun addEvent() {
        val name = newName.trim()
        val date = pickedMillis
        if (name.isEmpty() || date == null) return
        events = events + AnniversaryEvent(name, date)
        saveEvents(ctx, events)
        newName = ""
        pickedMillis = null
        showAdd = false
        SoundManager.playSuccess()
    }

    fun deleteEvent(e: AnniversaryEvent) {
        events = events.filterNot { it == e }
        saveEvents(ctx, events)
        SoundManager.playTap()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBack() }) { Text("‹") }
            Text(strings.toolTitles["anniversary"] ?: "Anniversary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = { haptic.performIfEnabled(); showAdd = true }) { Text("＋") }
        }

        if (events.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(strings.annivEmpty, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { haptic.performIfEnabled(); showAdd = true }) { Text(strings.annivAddFirst) }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(events) { e ->
                    val diff = daysBetween(e.dateMillis)
                    val dateText = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
                        .format(java.util.Date(e.dateMillis))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(e.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(
                                text = when {
                                    diff > 0 -> strings.annivPassed.format(diff)
                                    diff == 0L -> strings.annivToday
                                    else -> strings.annivComing.format(-diff)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(dateText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (diff >= 0) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (diff >= 0) "${strings.annivDay} $diff" else "${strings.annivDay} ${-diff}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { haptic.performIfEnabled(); deleteEvent(e) },
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text(strings.annivAdd) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text(strings.annivName) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { haptic.performIfEnabled(); showPicker = true }) {
                        Text(
                            pickedMillis?.let {
                                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date(it))
                            } ?: strings.annivPickDate
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    haptic.performIfEnabled()
                    addEvent()
                }) { Text(strings.annivSave) }
            },
            dismissButton = {
                TextButton(onClick = { haptic.performIfEnabled(); showAdd = false }) { Text(strings.annivCancel) }
            }
        )
    }

    if (showPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = pickedMillis)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    haptic.performIfEnabled()
                    pickedMillis = state.selectedDateMillis
                    showPicker = false
                }) { Text(strings.annivSave) }
            },
            dismissButton = {
                TextButton(onClick = { haptic.performIfEnabled(); showPicker = false }) { Text(strings.annivCancel) }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
