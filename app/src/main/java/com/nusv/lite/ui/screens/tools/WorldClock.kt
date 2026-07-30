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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.ui.components.GlassCard
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone

private data class CityClock(
    val name: String,
    val country: String,
    val timeZone: String,
    val offset: Int,
)

private val cities = listOf(
    CityClock("Beijing", "中国", "Asia/Shanghai", 8),
    CityClock("Tokyo", "日本", "Asia/Tokyo", 9),
    CityClock("Seoul", "韩国", "Asia/Seoul", 9),
    CityClock("Singapore", "新加坡", "Asia/Singapore", 8),
    CityClock("London", "英国", "Europe/London", 0),
    CityClock("Paris", "法国", "Europe/Paris", 1),
    CityClock("Berlin", "德国", "Europe/Berlin", 1),
    CityClock("New York", "美国", "America/New_York", -5),
    CityClock("Los Angeles", "美国", "America/Los_Angeles", -8),
    CityClock("Sydney", "澳大利亚", "Australia/Sydney", 11),
    CityClock("Dubai", "阿联酋", "Asia/Dubai", 4),
    CityClock("Moscow", "俄罗斯", "Europe/Moscow", 3),
    CityClock("Mumbai", "印度", "Asia/Kolkata", 5),
    CityClock("Auckland", "新西兰", "Pacific/Auckland", 13),
    CityClock("Honolulu", "夏威夷", "Pacific/Honolulu", -10),
)

@Composable
fun WorldClock(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            tick = System.currentTimeMillis()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
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
            Spacer(Modifier.width(12.dp))
            Text("World Clock", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(cities) { city ->
                CityCard(city)
            }
        }
    }
}

@Composable
private fun CityCard(city: CityClock) {
    val tz = TimeZone.getTimeZone(city.timeZone)
    val now = Calendar.getInstance(tz)
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)
    val second = now.get(Calendar.SECOND)
    val time = String.format("%02d:%02d:%02d", hour, minute, second)

    val offsetMillis = tz.getOffset(System.currentTimeMillis())
    val absMillis = kotlin.math.abs(offsetMillis)
    val offsetHours = offsetMillis / 3600000
    val offsetMinutes = (absMillis % 3600000) / 60000

    val sign = if (offsetMillis >= 0) "+" else "-"
    val offsetStr = if (offsetMinutes > 0) {
        "GMT$sign${kotlin.math.abs(offsetHours)}:${String.format("%02d", offsetMinutes)}"
    } else {
        "GMT$sign${kotlin.math.abs(offsetHours)}"
    }

    val isDay = hour in 6..17
    val tint = if (isDay) Color(0xFFFFF8E1) else Color(0xFF1A1A2E)

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(tint.copy(alpha = 0.12f)),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = offsetStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = offsetStr,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (isDay) "Day" else "Night",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
