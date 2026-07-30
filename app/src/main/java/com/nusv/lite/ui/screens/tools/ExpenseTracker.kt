package com.nusv.lite.ui.screens.tools

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nusv.lite.util.performIfEnabled

data class Expense(val id: Long, val description: String, val amount: Double, val category: String)

private val categories = listOf("Food", "Transport", "Shopping", "Entertainment", "Bills", "Other")

@Composable
fun ExpenseTracker(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val descState = remember { mutableStateOf("") }
    val amountState = remember { mutableStateOf("") }
    val catState = remember { mutableStateOf(categories[0]) }
    val expenses = remember { mutableStateListOf<Expense>() }
    var nextId = remember { mutableStateOf(1L) }

    val total = expenses.sumOf { it.amount }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() }, contentAlignment = Alignment.Center) {
                Text("\u2190", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Text("Expense Tracker", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(20.dp))

        Text("Total: \$${String.format("%.2f", total)}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(Modifier.height(16.dp))

        BasicTextField(
            value = descState.value,
            onValueChange = { descState.value = it },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp),
            decorationBox = { inner ->
                if (descState.value.isEmpty()) Text("Description", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                inner()
            }
        )

        Spacer(Modifier.height(8.dp))

        BasicTextField(
            value = amountState.value,
            onValueChange = { amountState.value = it.filter { c -> c.isDigit() || c == '.' } },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp),
            decorationBox = { inner ->
                if (amountState.value.isEmpty()) Text("0.00", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                inner()
            }
        )

        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                Box(modifier = Modifier.background(if (catState.value == cat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp)).clickable { haptic.performIfEnabled(); catState.value = cat }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(cat, style = MaterialTheme.typography.labelLarge, color = if (catState.value == cat) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable {
            haptic.performIfEnabled()
            val amount = amountState.value.toDoubleOrNull()
            if (descState.value.isNotBlank() && amount != null) {
                expenses.add(Expense(nextId.value++, descState.value, amount, catState.value))
                descState.value = ""
                amountState.value = ""
            }
        }, contentAlignment = Alignment.Center) {
            Text("Add", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(expenses.toList(), key = { it.id }) { expense ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF4CAF50), RoundedCornerShape(5.dp)))
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(expense.description, style = MaterialTheme.typography.bodyLarge)
                        Text(expense.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("\$${String.format("%.2f", expense.amount)}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.width(8.dp))
                    Box(modifier = Modifier.size(24.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).clickable { haptic.performIfEnabled(); expenses.remove(expense) }, contentAlignment = Alignment.Center) {
                        Text("\u00D7", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
