package com.nusv.lite.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay

private val fantasyFirst = listOf("Aelar", "Baelon", "Caelum", "Dorian", "Eldrin", "Faelan", "Gareth", "Hadrian", "Ithil", "Joral", "Kaelen", "Lorien", "Maelor", "Nyx", "Orien", "Phaelan", "Quorin", "Rael", "Sylas", "Thalion", "Ulric", "Valen", "Wren", "Xandar", "Yorick", "Zephyr")
private val fantasyLast = listOf("Shadowbane", "Stormbringer", "Moonshadow", "Ironheart", "Dawnweaver", "Fireforge", "Windwalker", "Starfall", "Nightwhisper", "Brightwood", "Silverstream", "Greycastle", "Oakenshield", "Ravencrest", "Goldmoon")

private val japaneseFirst = listOf("Haruto", "Yuito", "Sota", "Minato", "Hinata", "Riko", "Sakura", "Yuna", "Aoi", "Mei", "Ren", "Sora", "Kaito", "Riku", "Yuki", "Hana", "Akari", "Koharu", "Rin", "Tsubasa")
private val japaneseLast = listOf("Sato", "Suzuki", "Takahashi", "Tanaka", "Watanabe", "Ito", "Yamamoto", "Nakamura", "Ogawa", "Kato", "Yoshida", "Yamada", "Sasaki", "Yamaguchi", "Matsumoto")

private val englishFirstM = listOf("James", "Oliver", "William", "Henry", "Jack", "Leo", "Owen", "Lucas", "Thomas", "Ethan", "Noah", "Liam", "Mason", "Logan", "Alexander", "Daniel", "Matthew", "Samuel", "David", "Joseph")
private val englishFirstF = listOf("Emma", "Olivia", "Ava", "Sophia", "Isabella", "Mia", "Charlotte", "Amelia", "Harper", "Evelyn", "Luna", "Chloe", "Penelope", "Layla", "Riley", "Ellie", "Nora", "Hazel", "Violet", "Aurora")
private val englishLast = listOf("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin")

private val scifiFirst = listOf("Nova", "Orion", "Vega", "Atlas", "Lyra", "Phoenix", "Cosmo", "Andromeda", "Rigel", "Sirius", "Nebula", "Titan", "Astra", "Comet", "Lunar", "Solaris", "Draco", "Electra", "Zenith", "Aurora")
private val scifiLast = listOf("Xerxes-7", "Nexus-\u0394", "Quantum", "\u03a9mega", "Starforge", "Voidwalker", "Lightbringer", "CyberCore", "Astralis", "Frostbyte")

private val chineseFirstM = listOf("Wei", "Jun", "Hao", "Lei", "Ming", "Feng", "Qiang", "Yong", "Gang", "Jian", "Long", "Tao", "Kai", "Peng", "Bin")
private val chineseFirstF = listOf("Yue", "Xin", "Li", "Juan", "Fang", "Yan", "Lin", "Hui", "Xia", "Yun", "Ting", "Mei", "Na", "Ping", "Rong")
private val chineseLast = listOf("Wang", "Li", "Zhang", "Liu", "Chen", "Yang", "Huang", "Zhao", "Wu", "Zhou", "Xu", "Sun", "Ma", "Zhu", "Hu")

private val categories = listOf("Fantasy", "Japanese", "English", "Sci-Fi", "Chinese")
private val genders = listOf("Any", "Male", "Female")
private val counts = listOf(1, 5, 10, 20)

private fun generateNames(category: String, gender: String, count: Int): List<String> {
    return when (category) {
        "Fantasy" -> fantasyFirst.shuffled().map { "${it} ${fantasyLast.random()}" }.take(count)
        "Japanese" -> japaneseFirst.shuffled().map { "${japaneseLast.random()} ${it}" }.take(count)
        "English" -> {
            val first = if (gender == "Male") englishFirstM else if (gender == "Female") englishFirstF else (englishFirstM + englishFirstF)
            first.shuffled().map { "${it} ${englishLast.random()}" }.take(count)
        }
        "Sci-Fi" -> scifiFirst.shuffled().map { "${it} ${scifiLast.random()}" }.take(count)
        "Chinese" -> {
            val first = if (gender == "Male") chineseFirstM else if (gender == "Female") chineseFirstF else (chineseFirstM + chineseFirstF)
            first.shuffled().map { "${chineseLast.random()} ${it}" }.take(count)
        }
        else -> emptyList()
    }
}

@Composable
fun RandomNameGenerator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var category by remember { mutableStateOf("Fantasy") }
    var gender by remember { mutableStateOf("Any") }
    var selectedCount by remember { mutableIntStateOf(5) }
    var names by remember { mutableStateOf(emptyList<String>()) }
    var copiedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(copiedIndex) {
        if (copiedIndex >= 0) { delay(1500); copiedIndex = -1 }
    }

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
            Text("Name Generator", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { cat ->
                val selected = category == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            haptic.performIfEnabled()
                            category = cat
                            if (cat != "English" && cat != "Chinese") gender = "Any"
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text(
                        cat,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (category == "English" || category == "Chinese") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                genders.forEach { g ->
                    val selected = gender == g
                    Box(
                        modifier = Modifier.weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                haptic.performIfEnabled()
                                gender = g
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            g,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            counts.forEach { c ->
                val selected = selectedCount == c
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            haptic.performIfEnabled()
                            selectedCount = c
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        c.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (names.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        haptic.performIfEnabled()
                        names = generateNames(category, gender, selectedCount)
                        copiedIndex = -1
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Generate",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        .clickable {
                            haptic.performIfEnabled()
                            val text = names.joinToString("\n")
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                            }
                            context.startActivity(Intent.createChooser(intent, null))
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Share All",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            haptic.performIfEnabled()
                            names = generateNames(category, gender, selectedCount)
                            copiedIndex = -1
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Regenerate",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(names) { name ->
                val idx = names.indexOf(name)
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            haptic.performIfEnabled()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("name", name))
                            copiedIndex = idx
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    if (copiedIndex == idx) {
                        Text(
                            "Copied!",
                            modifier = Modifier.align(Alignment.CenterEnd),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
