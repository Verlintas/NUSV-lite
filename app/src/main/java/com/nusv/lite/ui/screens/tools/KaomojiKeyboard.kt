package com.nusv.lite.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class KaomojiCategory(val name: String, val items: List<String>)

private val kaomojiData = listOf(
    KaomojiCategory("Happy", listOf(
        "(◕‿◕)", "(✿◠‿◠)", "(ノ＾Д＾)ﾉ", "＼(≧▽≦)／", "(*≧ω≦*)",
        "(●´ω｀●)", "★~(◡‿◡✿)", "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", "ヽ(○´∀`)ﾉ♪", "♪ヽ(´▽｀)/",
        "ヾ(●⌒∇⌒●)ﾉ", "(´▽`ʃ♡ƪ)", "✧*:･ﾟ✧(◕‿◕✿)✧*:･ﾟ✧", "ヽ(✿ﾟ▽ﾟ)ノ", "＼(＾▽＾)／",
        "(｡◕‿◕｡)", "✿ヽ(°▽°)ノ✿", "ヽ(★ω★)ﾉ", "(*¯︶¯*)", "♡(>ᴗ•)",
        "ヾ(〃^∇^)ﾉ", "ヽ(*・ω・)ﾉ", "(◕‿◕)♡", "✿✧ヽ(｡◕‿◕｡)ﾉ✧✿",
    )),
    KaomojiCategory("Sad", listOf(
        "(╥﹏╥)", "｡ﾟ(ﾟ´Д｀ﾟ)ﾟ｡", "(　；∀；)", "(´;ω;｀)", "(つω`｡)",
        "(╥_╥)", "(T_T)", "(｡•́︿•̀｡)", "(◕‿◕\u200B◕‿◕)", "ヽ(●-`Д´-)ノ",
        "(╯︵╰)", "（ｉДｉ）", "(༎ຶ⌑༎ຶ)", "(◞ ‸ ◟ㆀ)", "( ˘ ³˘)♥... no",
        "(;﹏;)", "｡:ﾟ(;´∩`;)ﾟ:｡", "૮(ꂧ᷆﹏ꂧ᷇)ა", "(｡•́︿•̀｡)", "｡ﾟ(｡ﾉωヽ｡)ﾟ｡",
    )),
    KaomojiCategory("Love", listOf(
        "(´♡‿♡`)", "(◕‿◕)♡", "♡(ӦｖӦ｡)", "ヽ(♡‿♡)ノ", "♡~ヾ(°▽°)ノ♡",
        "(*♡∀♡)", "(｡♡‿♡｡)", "♥(✿ฺ´∀`✿ฺ)ﾉ", "(◕‿◕✿)♥", "♡( ◡‿◡ )",
        "♥(๑¯◡¯๑)♥", "♡(˃͈ દ ˂͈ ༶ )", "(💕ᵒ̴̶̷̥́～ᵒ̴̶̷̣̥̀💕)", "(๑♡⌓♡๑)", "♡＼(￣▽￣)／♡",
        "✿♥‿♥✿", "(ʃƪ♡‿♡)", "♡(¬‿¬)", "٩(♡ε♡)۶", "♥( ´◡` )♥",
    )),
    KaomojiCategory("Angry", listOf(
        "(╬ Ò﹏Ó)", "ヽ(｀⌒´)ノ", "＼(｀0´)／", "(｀ε´)", "''(｀へ´)''",
        "(｀皿´＃)", "ヽ(≧Д≦)ノ", "(╯°□°)╯︵ ┻━┻", "ﾟヽ(｀□´)ﾉ", "(ノಠ益ಠ)ノ",
        "┬──┬ ノ( ゜-゜ノ)", "ლ(｀ー´ლ)", "(╬｀益´)ｺ", "ヽ(‵﹏´)ノ", "ψ(｀∇´)ψ",
    )),
    KaomojiCategory("Surprise", listOf(
        "(⊙_⊙)", "(°ロ°)", "(°△°)!!", "Σ(°△°|||)︴", "Σ(ﾟДﾟ；)",
        "ヽ(°〇°)ﾉ", "(°Д°)", "(๑°ㅁ°๑)", "✧(≖ ◡ ≖✿)", "(ʘдʘ)",
        "∑(O_O;)", "ヽ(ﾟ〇ﾟ)ﾉ", "!!!(⊙⊙)!!!", "(☉_☉)", "°Д°",
    )),
    KaomojiCategory("Animals", listOf(
        "(=^‥^=)", "(^._.^)ﾉ", "ヾ(=^▽^=)ノ", "／(･ × ･)＼", "(U・x・U)",
        "／(=∵=)＼", "／(≧ x ≦)＼", "⊂(￣(ｴ)￣)⊃", "⊂(・(ェ)・)⊃", "ᓚᘏᗢ",
        "ˁ˙˟˙ˀ", "／(=′◡`=)＼", "ᶘ ᵒᴥᵒᶅ", "ʕ•̫͡•ʔ", "ʕ•ᴥ•ʔ",
        "🐱(=^･ｪ･^=)🐱", "🐶U´꓃`U🐶", "🐰／(=´x`=)＼🐰", "🐻ʕ•̫͡•ʔ🐻", "🐼(｡◕‿◕｡)🐼",
    )),
    KaomojiCategory("Actions", listOf(
        "┌(☆0☆)┐", "┐(￣∀￣)┌", "ヽ(￣д￣;)ﾉ", "┐(´д｀)┌", "(￣ω￣;)",
        "ヽ(´ー｀)ﾉ", "＼(￣O￣)／", "(￣▽￣)ノ", "ヾ(￣▽￣)ﾉ", "ヽ(^。^)丿",
        "d(￣◇￣)b", "ヽ(｡◕‿◕｡)ﾉ", "┗(＾0＾)┓", "┏(＾0＾)┛", "┗(＾▽＾)┓",
    )),
    KaomojiCategory("Misc", listOf(
        "(￣▽￣)~*", "→_→", "←_←", "↖(^ω^)↗", "↗(^ω^)↖",
        "凸(￣ヘ￣)凸", "( ͡° ͜ʖ ͡°)", "¯\\_(ツ)_/¯", "ಠ_ಠ", "🇧🇲👿🇧🇲",
        "☜(⌒▽⌒)☞", "☝( ◠‿◠ )☝", "✌(◕‿◕)✌", "👍(◕‿◕)👍", "👎(◕‿◕)👎",
        "🎵♪♬♩🎶", "★ﾟ*｡ﾟ+*.♡*.+ﾟ｡*ﾟ★", "✿‿✿", "(｀∀´)Ψ", "☆*:.｡.o(≧▽≦)o.｡.:*☆",
    )),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KaomojiKeyboard(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var copiedKaomoji by remember { mutableStateOf<String?>(null) }

    val filteredCategories = remember(searchQuery) {
        if (searchQuery.isBlank()) kaomojiData
        else kaomojiData.map { cat ->
            cat.copy(items = cat.items.filter { it.contains(searchQuery, ignoreCase = true) })
        }.filter { it.items.isNotEmpty() }
    }

    val displayItems = if (searchQuery.isNotBlank()) {
        filteredCategories.flatMap { it.items }
    } else {
        kaomojiData.getOrNull(selectedCategory)?.items ?: emptyList()
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
            Text("Kaomoji", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDD0D", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        if (searchQuery.isEmpty()) {
                            Text(
                                "Search kaomoji...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }

        Spacer(Modifier.height(12.dp))

        if (searchQuery.isBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                kaomojiData.forEachIndexed { index, category ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedCategory == index) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable {
                                haptic.performIfEnabled()
                                selectedCategory = index
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            category.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedCategory == index)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        ) {
            Column {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    displayItems.forEach { kaomoji ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    haptic.performIfEnabled()
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("kaomoji", kaomoji))
                                    copiedKaomoji = kaomoji
                                    scope.launch {
                                        delay(1200)
                                        if (copiedKaomoji == kaomoji) copiedKaomoji = null
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            Column {
                                Text(
                                    kaomoji,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                AnimatedVisibility(
                                    visible = copiedKaomoji == kaomoji,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                ) {
                                    Text(
                                        "Copied!",
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
        }
    }
}
