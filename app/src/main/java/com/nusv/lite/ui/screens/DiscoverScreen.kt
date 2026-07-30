package com.nusv.lite.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.InvertColors
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Spellcheck
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import com.nusv.lite.ui.components.GlassCard
import com.nusv.lite.ui.screens.minigames.TicTacToe
import com.nusv.lite.ui.screens.minigames.Game2048
import com.nusv.lite.ui.screens.minigames.Minesweeper
import com.nusv.lite.ui.screens.minigames.MemoryMatch
import com.nusv.lite.ui.screens.minigames.SnakeGame
import com.nusv.lite.ui.screens.minigames.WordleGame
import com.nusv.lite.ui.screens.minigames.SimonGame
import com.nusv.lite.ui.screens.minigames.WhackAMole
import com.nusv.lite.ui.screens.tools.CurrencyConverter
import com.nusv.lite.ui.screens.tools.WorldClock
import com.nusv.lite.ui.screens.tools.IntervalTimer
import com.nusv.lite.ui.screens.tools.ExpenseTracker
import com.nusv.lite.ui.screens.tools.ScientificCalculator
import com.nusv.lite.ui.screens.tools.CanvasDrawing
import com.nusv.lite.ui.screens.tools.MarkdownPreview
import com.nusv.lite.ui.screens.tools.Biorhythm
import com.nusv.lite.ui.screens.tools.Metronome
import com.nusv.lite.ui.screens.tools.MorseCode
import com.nusv.lite.ui.screens.tools.QRGenerator
import com.nusv.lite.ui.screens.tools.KaomojiKeyboard
import com.nusv.lite.ui.screens.tools.RandomQuotes
import com.nusv.lite.ui.screens.tools.RandomNameGenerator
import com.nusv.lite.util.ClickTracker
import com.nusv.lite.util.LayoutMode
import com.nusv.lite.util.LayoutPrefs
import com.nusv.lite.util.LocalAppStrings
import com.nusv.lite.util.PointsManager
import com.nusv.lite.util.performIfEnabled
import com.nusv.lite.util.scalePress
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.os.BatteryManager

enum class Category(val labelKey: String) {
    ALL("catAll"),
    GAMES("catGames"),
    UTILITIES("catUtilities"),
    DEV_TOOLS("catDevtools"),
    OTHER("catOther")
}

data class MiniApp(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconFilled: ImageVector,
    val category: Category = Category.OTHER
)

val miniApps = listOf(
    MiniApp("dice", "Dice Roller", "Roll d4/d6/d8/d12/d20/d100", Icons.Outlined.Casino, Icons.Filled.Casino, Category.GAMES),
    MiniApp("coin", "Coin Flip", "Heads or tails", Icons.Outlined.Shuffle, Icons.Filled.Shuffle, Category.GAMES),
    MiniApp("eightball", "Magic 8-Ball", "Ask a yes/no question", Icons.Outlined.QuestionMark, Icons.Filled.QuestionMark, Category.GAMES),
    MiniApp("rps", "Rock Paper Scissors", "Play against the AI", Icons.Outlined.SportsEsports, Icons.Filled.SportsEsports, Category.GAMES),
    MiniApp("slot", "Slot Machine", "Pull the lever and win", Icons.Outlined.Casino, Icons.Filled.Casino, Category.GAMES),
    MiniApp("tip", "Tip Calculator", "Calculate the tip", Icons.Outlined.Calculate, Icons.Filled.Calculate, Category.UTILITIES),
    MiniApp("bmi", "BMI Calculator", "Body mass index", Icons.Outlined.Favorite, Icons.Filled.Favorite, Category.UTILITIES),
    MiniApp("random", "Random Number", "Generate random numbers", Icons.Outlined.Tag, Icons.Filled.Tag, Category.UTILITIES),
    MiniApp("unit", "Unit Converter", "cm/in, °C/°F, kg/lb", Icons.Outlined.SwapHoriz, Icons.Filled.SwapHoriz, Category.UTILITIES),
    MiniApp("stopwatch", "Stopwatch", "Lap timing", Icons.Outlined.Timer, Icons.Filled.Timer, Category.UTILITIES),
    MiniApp("countdown", "Countdown", "Set and count down", Icons.Outlined.Alarm, Icons.Filled.Alarm, Category.UTILITIES),
    MiniApp("textcount", "Text Counter", "Character & word count", Icons.Outlined.Description, Icons.Filled.Description, Category.UTILITIES),
    MiniApp("age", "Age Calculator", "Calculate your age", Icons.Outlined.DateRange, Icons.Filled.DateRange, Category.UTILITIES),
    MiniApp("password", "Password Gen", "Generate secure passwords", Icons.Outlined.Lock, Icons.Filled.Lock, Category.UTILITIES),
    MiniApp("pct", "Percentage", "X% of Y, X is what % of Y", Icons.Outlined.Calculate, Icons.Filled.Calculate, Category.UTILITIES),
    MiniApp("base", "Number Base", "Dec / Bin / Hex / Oct", Icons.Outlined.Build, Icons.Filled.Build, Category.UTILITIES),
    MiniApp("split", "Split Bill", "Split expenses evenly", Icons.Outlined.Person, Icons.Filled.Person, Category.UTILITIES),
    MiniApp("datediff", "Date Diff", "Days between two dates", Icons.Outlined.Event, Icons.Filled.Event, Category.UTILITIES),
    MiniApp("pomo", "Pomodoro", "25min focus timer", Icons.Outlined.PlayArrow, Icons.Filled.PlayArrow, Category.UTILITIES),
    MiniApp("decide", "Decision Maker", "Let fate decide", Icons.Outlined.CheckCircle, Icons.Filled.CheckCircle, Category.UTILITIES),
    MiniApp("palindrome", "Palindrome", "Check text palindrome", Icons.Outlined.Refresh, Icons.Filled.Refresh, Category.UTILITIES),
    MiniApp("todo", "Todo List", "Simple task manager", Icons.Outlined.List, Icons.Filled.List, Category.UTILITIES),
    MiniApp("json", "JSON Formatter", "Validate & pretty-print JSON", Icons.Outlined.Code, Icons.Filled.Code, Category.DEV_TOOLS),
    MiniApp("base64", "Base64 Tool", "Encode / decode Base64", Icons.Outlined.VpnKey, Icons.Filled.VpnKey, Category.DEV_TOOLS),
    MiniApp("color", "Color Converter", "HEX ↔ RGB ↔ HSL", Icons.Outlined.InvertColors, Icons.Filled.InvertColors, Category.DEV_TOOLS),
    MiniApp("uuid", "UUID Generator", "Generate UUID v4", Icons.Outlined.Star, Icons.Filled.Star, Category.DEV_TOOLS),
    MiniApp("hash", "Hash Generator", "MD5 / SHA-1 / SHA-256", Icons.Outlined.Security, Icons.Filled.Security, Category.DEV_TOOLS),
    MiniApp("epoch", "Epoch Converter", "Unix timestamp ↔ date", Icons.Outlined.Schedule, Icons.Filled.Schedule, Category.DEV_TOOLS),
    MiniApp("url", "URL Tool", "Encode / decode URLs", Icons.Outlined.Public, Icons.Filled.Public, Category.DEV_TOOLS),
    MiniApp("case", "Case Converter", "camelCase, snake_case, etc", Icons.Outlined.Spellcheck, Icons.Filled.Spellcheck, Category.DEV_TOOLS),
    MiniApp("regex", "Regex Tester", "Test regular expressions", Icons.Outlined.Search, Icons.Filled.Search, Category.DEV_TOOLS),
    MiniApp("lorem", "Lorem Ipsum", "Generate placeholder text", Icons.Outlined.Description, Icons.Filled.Description, Category.DEV_TOOLS),
    MiniApp("breathing", "Breathing", "Inhale / Hold / Exhale", Icons.Outlined.SelfImprovement, Icons.Filled.SelfImprovement, Category.UTILITIES),
    // New tools
    MiniApp("tictactoe", "Tic Tac Toe", "Classic 3-in-a-row", Icons.Outlined.SportsEsports, Icons.Filled.SportsEsports, Category.GAMES),
    MiniApp("2048", "2048", "Slide and merge tiles", Icons.Outlined.Casino, Icons.Filled.Casino, Category.GAMES),
    MiniApp("minesweeper", "Minesweeper", "Clear the minefield", Icons.Outlined.Security, Icons.Filled.Security, Category.GAMES),
    MiniApp("memory", "Memory Match", "Find matching pairs", Icons.Outlined.Favorite, Icons.Filled.Favorite, Category.GAMES),
    MiniApp("currency", "Currency Converter", "USD / EUR / JPY / CNY…", Icons.Outlined.SwapHoriz, Icons.Filled.SwapHoriz, Category.UTILITIES),
    MiniApp("worldclock", "World Clock", "15 cities worldwide", Icons.Outlined.Schedule, Icons.Filled.Schedule, Category.UTILITIES),
    MiniApp("interval", "Interval Timer", "Work / Rest rounds", Icons.Outlined.Timer, Icons.Filled.Timer, Category.UTILITIES),
    MiniApp("expense", "Expense Tracker", "Track daily spending", Icons.Outlined.List, Icons.Filled.List, Category.UTILITIES),
    // Advanced tools v1.5.0
    MiniApp("calc", "Scientific Calc", "sin cos tan log sqrt", Icons.Outlined.Calculate, Icons.Filled.Calculate, Category.DEV_TOOLS),
    MiniApp("canvas", "Canvas Drawing", "Draw & save to gallery", Icons.Outlined.Create, Icons.Filled.Create, Category.UTILITIES),
    MiniApp("mdpreview", "Markdown Preview", "Live markdown render", Icons.Outlined.Code, Icons.Filled.Code, Category.DEV_TOOLS),
    MiniApp("biorhythm", "Biorhythm", "Physical / Emotional / Intellectual", Icons.Outlined.Favorite, Icons.Filled.Favorite, Category.UTILITIES),
    MiniApp("metronome", "Metronome", "BPM tap tempo beat", Icons.Outlined.Timer, Icons.Filled.Timer, Category.UTILITIES),
    MiniApp("morse", "Morse Code", "Text ↔ Morse with vibration", Icons.Outlined.VpnKey, Icons.Filled.VpnKey, Category.DEV_TOOLS),
    // Generative / Creative tools
    MiniApp("qr", "QR Generator", "Create QR codes instantly", Icons.Outlined.Code, Icons.Filled.Code, Category.UTILITIES),
    MiniApp("kaomoji", "Kaomoji", "Emoticon keyboard & faces", Icons.Outlined.Favorite, Icons.Filled.Favorite, Category.OTHER),
    MiniApp("quotes", "Random Quotes", "Inspiration & wisdom", Icons.Outlined.Star, Icons.Filled.Star, Category.OTHER),
    MiniApp("namer", "Name Generator", "Fantasy / Japanese / Sci-Fi…", Icons.Outlined.Person, Icons.Filled.Person, Category.OTHER),
    MiniApp("snake", "Snake", "Classic snake game", Icons.Outlined.PlayArrow, Icons.Filled.PlayArrow, Category.GAMES),
    MiniApp("wordle", "Wordle", "Guess the 5-letter word", Icons.Outlined.Spellcheck, Icons.Filled.Spellcheck, Category.GAMES),
    MiniApp("simon", "Simon Says", "Memory sequence game", Icons.Outlined.Star, Icons.Filled.Star, Category.GAMES),
    MiniApp("whack", "Whack-a-Mole", "Tap the moles!", Icons.Outlined.SportsEsports, Icons.Filled.SportsEsports, Category.GAMES),
    MiniApp("flashlight", "Flashlight", "Screen torch light", Icons.Outlined.FlashOn, Icons.Filled.FlashOn, Category.UTILITIES),
    MiniApp("battery", "Battery Info", "Battery level & status", Icons.Outlined.BatteryFull, Icons.Filled.BatteryFull, Category.UTILITIES),
    MiniApp("quicktimer", "Quick Timer", "1 / 3 / 5 / 10 min presets", Icons.Outlined.Timer, Icons.Filled.Timer, Category.UTILITIES),
)

val orcaHiddenTools = listOf(
    MiniApp("orcamatrix", "Matrix Rain", "\uD83D\uDDA4 Orca exclusive: digital rain", Icons.Outlined.Code, Icons.Filled.Code, Category.OTHER),
    MiniApp("orcasecret", "Secret Vault", "\uD83D\uDDA4 Orca exclusive: hidden notes", Icons.Outlined.Lock, Icons.Filled.Lock, Category.OTHER),
)

var _pendingToolId: String? = null

@Composable
fun DiscoverScreen() {
    var activeApp by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf(Category.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val clickCounts = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    LaunchedEffect(Unit) {
        clickCounts.value = ClickTracker.getCounts(context)
        _pendingToolId?.let {
            activeApp = it
            _pendingToolId = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Text(
            text = strings.discoverTitle,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = strings.discoverSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorationBox = { inner ->
                        if (searchQuery.isEmpty()) {
                            Text("Search tools…", style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        inner()
                    },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                )
                if (searchQuery.isNotEmpty()) {
                    Text("\u2715", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { haptic.performIfEnabled(); searchQuery = "" })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Category.entries.toList().forEach { cat ->
                item {
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedCategory == cat) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            selectedCategory = cat
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val catLabel = when (cat) {
                        Category.ALL -> strings.catAll
                        Category.GAMES -> strings.catGames
                        Category.UTILITIES -> strings.catUtilities
                        Category.DEV_TOOLS -> strings.catDevtools
                        Category.OTHER -> strings.catOther
                    }
                    Text(
                        text = catLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedCategory == cat) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                }
            }
        }

        AnimatedContent(
            targetState = activeApp,
            transitionSpec = {
                fadeIn(tween(180)) togetherWith fadeOut(tween(120))
            },
            label = "toolSwitch"
        ) { currentApp ->
            if (currentApp == null) {
                val layoutMode = LayoutPrefs.get()
                val query = searchQuery.lowercase()
                val orcaActive = PointsManager.isOrcaActive(context)
                val allApps = if (orcaActive) miniApps + orcaHiddenTools else miniApps
                val sorted = allApps
                    .filter { (selectedCategory == Category.ALL || it.category == selectedCategory) &&
                        (query.isEmpty() || it.title.lowercase().contains(query) || it.description.lowercase().contains(query)) }
                    .sortedByDescending { clickCounts.value[it.id] ?: 0 }
                if (layoutMode == LayoutMode.LIST) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sorted) { app ->
                            MiniAppRow(
                                app = app,
                                searchQuery = searchQuery,
                                onClick = {
                                    ClickTracker.increment(context, app.id)
                                    clickCounts.value = clickCounts.value.toMutableMap().apply { put(app.id, (this[app.id] ?: 0) + 1) }
                                    activeApp = app.id
                                }
                            )
                        }
                    }
                } else {
                    val columns = if (layoutMode == LayoutMode.GRID_2) 2 else 3
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sorted) { app ->
                            MiniAppGridCard(
                                app = app,
                                searchQuery = searchQuery,
                                onClick = {
                                    ClickTracker.increment(context, app.id)
                                    clickCounts.value = clickCounts.value.toMutableMap().apply { put(app.id, (this[app.id] ?: 0) + 1) }
                                    activeApp = app.id
                                }
                            )
                        }
                    }
                }
            } else {
                when (currentApp) {
                    "dice" -> DiceRoller(onBack = { activeApp = null })
                    "coin" -> CoinFlip(onBack = { activeApp = null })
                    "eightball" -> EightBall(onBack = { activeApp = null })
                    "rps" -> RockPaperScissors(onBack = { activeApp = null })
                    "slot" -> SlotMachine(onBack = { activeApp = null })
                    "tip" -> TipCalculator(onBack = { activeApp = null })
                    "bmi" -> BmiCalculator(onBack = { activeApp = null })
                    "random" -> RandomNumberGenerator(onBack = { activeApp = null })
                    "unit" -> UnitConverter(onBack = { activeApp = null })
                    "stopwatch" -> Stopwatch(onBack = { activeApp = null })
                    "countdown" -> Countdown(onBack = { activeApp = null })
                    "textcount" -> TextCounter(onBack = { activeApp = null })
                    "age" -> AgeCalculator(onBack = { activeApp = null })
                    "password" -> PasswordGenerator(onBack = { activeApp = null })
                    "pct" -> PercentageCalculator(onBack = { activeApp = null })
                    "base" -> NumberBaseConverter(onBack = { activeApp = null })
                    "split" -> SplitBill(onBack = { activeApp = null })
                    "datediff" -> DateDiffCalc(onBack = { activeApp = null })
                    "pomo" -> Pomodoro(onBack = { activeApp = null })
                    "decide" -> DecisionMaker(onBack = { activeApp = null })
                    "palindrome" -> PalindromeChecker(onBack = { activeApp = null })
                    "todo" -> TodoList(onBack = { activeApp = null })
                    "json" -> JsonFormatter(onBack = { activeApp = null })
                    "base64" -> Base64Tool(onBack = { activeApp = null })
                    "color" -> ColorConverter(onBack = { activeApp = null })
                    "uuid" -> UuidGenerator(onBack = { activeApp = null })
                    "hash" -> HashGenerator(onBack = { activeApp = null })
                    "epoch" -> EpochConverter(onBack = { activeApp = null })
                    "url" -> UrlTool(onBack = { activeApp = null })
                    "case" -> CaseConverter(onBack = { activeApp = null })
                    "regex" -> RegexTester(onBack = { activeApp = null })
                    "lorem" -> LoremIpsum(onBack = { activeApp = null })
                    "breathing" -> BreathingExercise(onBack = { activeApp = null })
                    "tictactoe" -> TicTacToe(onBack = { activeApp = null })
                    "2048" -> Game2048(onBack = { activeApp = null })
                    "minesweeper" -> Minesweeper(onBack = { activeApp = null })
                    "memory" -> MemoryMatch(onBack = { activeApp = null })
                    "currency" -> CurrencyConverter(onBack = { activeApp = null })
                    "worldclock" -> WorldClock(onBack = { activeApp = null })
                    "interval" -> IntervalTimer(onBack = { activeApp = null })
                    "expense" -> ExpenseTracker(onBack = { activeApp = null })
                    "calc" -> ScientificCalculator(onBack = { activeApp = null })
                    "canvas" -> CanvasDrawing(onBack = { activeApp = null })
                    "mdpreview" -> MarkdownPreview(onBack = { activeApp = null })
                    "biorhythm" -> Biorhythm(onBack = { activeApp = null })
                    "metronome" -> Metronome(onBack = { activeApp = null })
                    "morse" -> MorseCode(onBack = { activeApp = null })
                    "qr" -> QRGenerator(onBack = { activeApp = null })
                    "kaomoji" -> KaomojiKeyboard(onBack = { activeApp = null })
                    "quotes" -> RandomQuotes(onBack = { activeApp = null })
                    "namer" -> RandomNameGenerator(onBack = { activeApp = null })
                    "snake" -> SnakeGame(onBack = { activeApp = null })
                    "wordle" -> WordleGame(onBack = { activeApp = null })
                    "simon" -> SimonGame(onBack = { activeApp = null })
                    "whack" -> WhackAMole(onBack = { activeApp = null })
                    "flashlight" -> Flashlight(onBack = { activeApp = null })
                    "battery" -> BatteryInfo(onBack = { activeApp = null })
                    "quicktimer" -> QuickTimer(onBack = { activeApp = null })
                    "orcamatrix" -> MatrixRain(onBack = { activeApp = null })
                    "orcasecret" -> SecretVault(onBack = { activeApp = null })
                }
            }
        }
    }
}

@Composable
private fun highlightText(text: String, query: String): AnnotatedString {
    if (query.isEmpty()) return AnnotatedString(text)
    return buildAnnotatedString {
        append(text)
        val lower = text.lowercase()
        val q = query.lowercase()
        var start = lower.indexOf(q)
        while (start >= 0) {
            addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold), start, start + q.length)
            start = lower.indexOf(q, start + q.length)
        }
    }
}

@Composable
private fun MiniAppRow(app: MiniApp, searchQuery: String = "", onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .scalePress(interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { haptic.performIfEnabled(); onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = app.iconFilled,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = highlightText(strings.toolTitles[app.id] ?: app.title, searchQuery),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = highlightText(strings.toolDescs[app.id] ?: app.description, searchQuery),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MiniAppGridCard(app: MiniApp, searchQuery: String = "", onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val strings = LocalAppStrings.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .scalePress(interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { haptic.performIfEnabled(); onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = app.iconFilled,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = highlightText(strings.toolTitles[app.id] ?: app.title, searchQuery),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = highlightText(strings.toolDescs[app.id] ?: app.description, searchQuery),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun NusvMiniButton(text: String, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { haptic.performIfEnabled(); onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun AppHeader(title: String, onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .scalePress(interactionSource)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .clickable(interactionSource = interactionSource, indication = null) { haptic.performIfEnabled(); onBack() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\u2190",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

// ─── Dice Roller ──────────────────────────────────────────

@Composable
private fun DiceRoller(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val diceTypes = listOf(4, 6, 8, 12, 20, 100)
    var selectedSides by remember { mutableStateOf(6) }
    var result by remember { mutableStateOf<Int?>(null) }
    var rolling by remember { mutableStateOf(false) }
    val diceAnim by animateFloatAsState(
        targetValue = if (rolling || result == null) 0f else 1f,
        animationSpec = spring(dampingRatio = 0.3f, stiffness = 300f),
        label = "diceScale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Dice Roller", onBack)
        Spacer(Modifier.weight(0.3f))

        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(diceAnim)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (rolling) "..." else (result?.toString() ?: "?"),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            diceTypes.forEach { sides ->
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedSides == sides) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { haptic.performIfEnabled(); selectedSides = sides }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "d$sides",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedSides == sides) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clickable {
                    haptic.performIfEnabled()
                    scope.launch {
                        rolling = true
                        delay(300)
                        result = (1..selectedSides).random()
                        rolling = false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Roll",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.weight(0.5f))
    }
}

// ─── Coin Flip ────────────────────────────────────────────

@Composable
private fun CoinFlip(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<String?>(null) }
    var flipping by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipping) 360f else 0f,
        animationSpec = tween(600),
        label = "coinRotation"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Coin Flip", onBack)
        Spacer(Modifier.weight(0.3f))

        Box(
            modifier = Modifier
                .size(160.dp)
                .rotate(rotation)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(80.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when {
                    flipping -> "..."
                    result == null -> "?"
                    result == "heads" -> "H"
                    else -> "T"
                },
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        if (result != null) {
            Text(
                text = result!!.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clickable {
                    haptic.performIfEnabled()
                    scope.launch {
                        flipping = true
                        delay(600)
                        result = if (kotlin.random.Random.nextBoolean()) "heads" else "tails"
                        flipping = false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Flip",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.weight(0.5f))
    }
}

// ─── Magic 8-Ball ─────────────────────────────────────────

@Composable
private fun EightBall(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val answers = listOf(
        "It is certain.", "It is decidedly so.", "Without a doubt.",
        "Yes, definitely.", "You may rely on it.", "As I see it, yes.",
        "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
        "Reply hazy, try again.", "Ask again later.",
        "Better not tell you now.", "Cannot predict now.",
        "Concentrate and ask again.", "Don't count on it.",
        "My reply is no.", "My sources say no.",
        "Outlook not so good.", "Very doubtful."
    )
    var answer by remember { mutableStateOf<String?>(null) }
    var shaking by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Magic 8-Ball", onBack)
        Spacer(Modifier.weight(0.2f))

        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(90.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (shaking) "..." else "8",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Spacer(Modifier.height(20.dp))

        if (answer != null) {
            Text(
                text = answer!!,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        } else {
            Text(
                text = "Think of a question,\nthen tap the ball",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clickable {
                    haptic.performIfEnabled()
                    scope.launch {
                        shaking = true
                        delay(400)
                        answer = answers.random()
                        shaking = false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ask",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Rock Paper Scissors ──────────────────────────────────

private val rpsChoices = listOf("Rock", "Paper", "Scissors")

@Composable
private fun RockPaperScissors(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var playerChoice by remember { mutableStateOf<String?>(null) }
    var aiChoice by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Rock Paper Scissors", onBack)
        Spacer(Modifier.weight(0.15f))

        if (result != null) {
            Text(
                text = result!!,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "You: ${playerChoice}  vs  AI: ${aiChoice}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "Choose your move",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            rpsChoices.forEach { move ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            if (playerChoice == move) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            haptic.performIfEnabled()
                            playerChoice = move
                            aiChoice = rpsChoices.random()
                            result = when {
                                move == aiChoice -> "Draw"
                                (move == "Rock" && aiChoice == "Scissors") ||
                                (move == "Paper" && aiChoice == "Rock") ||
                                (move == "Scissors" && aiChoice == "Paper") -> "Win"
                                else -> "Lose"
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = move.first().toString(),
                        style = MaterialTheme.typography.displayMedium,
                        color = if (playerChoice == move) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (result != null) {
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(44.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .clickable {
                        haptic.performIfEnabled()
                        playerChoice = null
                        aiChoice = null
                        result = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Play again",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Tip Calculator ────────────────────────────────────────

@Composable
private fun TipCalculator(onBack: () -> Unit) {
    var bill by remember { mutableStateOf(50.0) }
    var tipPercent by remember { mutableStateOf(15) }

    val tipAmount = bill * tipPercent / 100.0
    val total = bill + tipAmount

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Tip Calculator", onBack)
        Spacer(Modifier.height(40.dp))

        Text("Bill Amount", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { bill = maxOf(0.0, bill - 1) }
            Text(
                text = "$${String.format("%.1f", bill)}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { bill = minOf(999.0, bill + 1) }
        }

        Spacer(Modifier.height(32.dp))
        Text("Tip Percentage", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { tipPercent = maxOf(0, tipPercent - 1) }
            Text(
                text = "$tipPercent%",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { tipPercent = minOf(100, tipPercent + 1) }
        }

        Spacer(Modifier.height(48.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tip", style = MaterialTheme.typography.bodyLarge)
                    Text("$${String.format("%.2f", tipAmount)}", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text("$${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── BMI Calculator ────────────────────────────────────────

@Composable
private fun BmiCalculator(onBack: () -> Unit) {
    var weight by remember { mutableStateOf(70.0) }
    var height by remember { mutableStateOf(175.0) }

    val bmi = if (height > 0) weight / ((height / 100) * (height / 100)) else 0.0
    val category = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("BMI Calculator", onBack)
        Spacer(Modifier.height(40.dp))

        Text("Weight (kg)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { weight = maxOf(20.0, weight - 1) }
            Text(
                text = "${weight.toInt()} kg",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { weight = minOf(300.0, weight + 1) }
        }

        Spacer(Modifier.height(32.dp))
        Text("Height (cm)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { height = maxOf(50.0, height - 1) }
            Text(
                text = "${height.toInt()} cm",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { height = minOf(300.0, height + 1) }
        }

        Spacer(Modifier.height(48.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.1f", bmi),
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Random Number Generator ───────────────────────────────

@Composable
private fun RandomNumberGenerator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var min by remember { mutableStateOf(1) }
    var max by remember { mutableStateOf(100) }
    var result by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Random Number", onBack)
        Spacer(Modifier.height(40.dp))

        Text("Min", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { min = maxOf(-9999, min - 1) }
            Text(
                text = "$min",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { min = minOf(9999, min + 1) }
        }

        Spacer(Modifier.height(32.dp))
        Text("Max", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { max = maxOf(-9999, max - 1) }
            Text(
                text = "$max",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            NusvMiniButton("+") { max = minOf(9999, max + 1) }
        }

        Spacer(Modifier.height(48.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Card(
                onClick = { haptic.performIfEnabled(); result = (min..max).random() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Generate",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 16.dp)
                )
            }
        }

        if (result != null) {
            Spacer(Modifier.height(32.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Result", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$result",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Unit Converter ────────────────────────────────────────

private data class Conversion(
    val label: String,
    val fromUnit: String,
    val toUnit: String,
    val convert: (Double) -> Double
)

private val conversions = listOf(
    Conversion("°C → °F", "°C", "°F", { it * 9 / 5 + 32 }),
    Conversion("°F → °C", "°F", "°C", { (it - 32) * 5 / 9 }),
    Conversion("cm → in", "cm", "in", { it / 2.54 }),
    Conversion("in → cm", "in", "cm", { it * 2.54 }),
    Conversion("kg → lb", "kg", "lb", { it * 2.20462 }),
    Conversion("lb → kg", "lb", "kg", { it / 2.20462 }),
    Conversion("km → mi", "km", "mi", { it / 1.60934 }),
    Conversion("mi → km", "mi", "km", { it * 1.60934 })
)

@Composable
private fun UnitConverter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var selectedIndex by remember { mutableStateOf(0) }
    var input by remember { mutableStateOf("100") }

    val conv = conversions[selectedIndex]
    val inputValue = input.toDoubleOrNull() ?: 0.0
    val outputValue = conv.convert(inputValue)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Unit Converter", onBack)
        Spacer(Modifier.height(20.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            conversions.indices.forEach { i ->
                item {
                    Box(
                        modifier = Modifier
                            .background(
                                if (i == selectedIndex) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { haptic.performIfEnabled(); selectedIndex = i }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversions[i].label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (i == selectedIndex) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))
        Text(conv.fromUnit, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = input,
            onValueChange = { input = it.filter { c -> c.isDigit() || c == '.' } },
            textStyle = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )

        Spacer(Modifier.height(48.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(conv.toUnit, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = String.format("%.4f", outputValue),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Stopwatch ─────────────────────────────────────────────

@Composable
private fun Stopwatch(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var elapsed by remember { mutableStateOf(0L) }
    var running by remember { mutableStateOf(false) }
    var laps by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(running) {
        while (running) {
            kotlinx.coroutines.delay(100)
            elapsed += 100
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Stopwatch", onBack)
        Spacer(Modifier.weight(0.15f))

        AnimatedContent(
            targetState = elapsed,
            transitionSpec = {
                fadeIn(tween(100)) togetherWith fadeOut(tween(100))
            },
            label = "timer"
        ) { e ->
            Text(
                text = String.format("%02d:%02d.%d", e / 60000, (e % 60000) / 1000, (e % 1000) / 100),
                style = MaterialTheme.typography.displayLarge
            )
        }

        Spacer(Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!running) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { haptic.performIfEnabled(); running = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Start", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { haptic.performIfEnabled(); running = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Stop", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        haptic.performIfEnabled()
                        laps = laps + String.format("%02d:%02d.%d", elapsed / 60000, (elapsed % 60000) / 1000, (elapsed % 1000) / 100)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Lap", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        haptic.performIfEnabled()
                        elapsed = 0; laps = emptyList(); running = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Reset", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (laps.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                laps.forEachIndexed { i, lap ->
                    item {
                        Text(
                            text = "Lap ${i + 1}: $lap",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        } else {
        Spacer(Modifier.weight(0.3f))
    }
}


}

// ─── Countdown Timer ───────────────────────────────────────

@Composable
private fun Countdown(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var totalSeconds by remember { mutableStateOf(60) }
    var remaining by remember { mutableStateOf(60) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running && remaining > 0) {
            kotlinx.coroutines.delay(1000)
            remaining--
        }
        if (remaining == 0 && running) {
            haptic.performIfEnabled()
            running = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Countdown", onBack)
        Spacer(Modifier.weight(0.2f))

        Text(
            text = String.format("%02d:%02d", remaining / 60, remaining % 60),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(Modifier.height(32.dp))

        if (!running) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(30, 60, 120, 300).forEach { secs ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (totalSeconds == secs) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { haptic.performIfEnabled(); totalSeconds = secs; remaining = secs }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (secs) { 30 -> "30s"; 60 -> "1m"; 120 -> "2m"; else -> "5m" },
                            style = MaterialTheme.typography.labelLarge,
                            color = if (totalSeconds == secs) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 40.dp).height(48.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); running = true },
                contentAlignment = Alignment.Center
            ) {
                Text("Start", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 40.dp).height(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); running = false },
                contentAlignment = Alignment.Center
            ) {
                Text("Stop", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (remaining <= 0) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 40.dp).height(44.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                        .clickable { haptic.performIfEnabled(); remaining = totalSeconds },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Reset", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Text Counter ──────────────────────────────────────────

@Composable
private fun TextCounter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var text by remember { mutableStateOf("") }
    var showWordCount by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Text Counter", onBack)
        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxSize(),
                decorationBox = { inner ->
                    if (text.isEmpty()) {
                        Text("Type or paste text here...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    inner()
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        val chars = text.length
        val charsNoSpace = text.count { !it.isWhitespace() }
        val words = text.split(Regex("\\s+")).count { it.isNotBlank() }
        val lines = text.count { it == '\n' } + 1

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        if (showWordCount) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { haptic.performIfEnabled(); showWordCount = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Words", style = MaterialTheme.typography.labelLarge,
                    color = if (showWordCount) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(
                modifier = Modifier
                    .background(
                        if (!showWordCount) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { haptic.performIfEnabled(); showWordCount = false }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Chars", style = MaterialTheme.typography.labelLarge,
                    color = if (!showWordCount) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(24.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (showWordCount) {
                    Text("$words", style = MaterialTheme.typography.displayMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("words", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("$chars characters  |  $lines lines", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Text("$chars", style = MaterialTheme.typography.displayMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("characters total", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("$charsNoSpace no space  |  $words words  |  $lines lines", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Age Calculator ────────────────────────────────────────

@Composable
private fun AgeCalculator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var year by remember { mutableStateOf(2000) }
    var month by remember { mutableStateOf(1) }
    var day by remember { mutableStateOf(1) }

    val now = java.util.Calendar.getInstance()
    val birth = java.util.Calendar.getInstance().apply { set(year, month - 1, day) }
    var years = now.get(java.util.Calendar.YEAR) - birth.get(java.util.Calendar.YEAR)
    var months = now.get(java.util.Calendar.MONTH) - birth.get(java.util.Calendar.MONTH)
    var days = now.get(java.util.Calendar.DAY_OF_MONTH) - birth.get(java.util.Calendar.DAY_OF_MONTH)
    if (days < 0) { months--; days += 30 }
    if (months < 0) { years--; months += 12 }

    val totalDays = ((now.timeInMillis - birth.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Age Calculator", onBack)
        Spacer(Modifier.height(24.dp))

        Text("Year", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { if (year > 1900) year-- }
            Text("$year", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("+") { if (year < now.get(java.util.Calendar.YEAR)) year++ }
        }
        Spacer(Modifier.height(16.dp))
        Text("Month", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { if (month > 1) month-- }
            Text("$month", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("+") { if (month < 12) month++ }
        }
        Spacer(Modifier.height(16.dp))
        Text("Day", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { if (day > 1) day-- }
            Text("$day", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("+") { if (day < 31) day++ }
        }

        Spacer(Modifier.height(32.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$years years  $months months  $days days", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("$totalDays days old", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Percentage Calculator ─────────────────────────────────

@Composable
private fun PercentageCalculator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var mode by remember { mutableStateOf(true) }
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Percentage", onBack)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.background(if (mode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); mode = true }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("X% of Y", style = MaterialTheme.typography.labelLarge, color = if (mode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(Modifier.background(if (!mode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); mode = false }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("X is ?% of Y", style = MaterialTheme.typography.labelLarge, color = if (!mode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(32.dp))
        Text(if (mode) "X (percentage)" else "X (part)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = num1, onValueChange = { num1 = it.filter { c -> c.isDigit() || c == '.' } }, textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(Modifier.height(24.dp))
        Text(if (mode) "Y (total)" else "Y (total)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = num2, onValueChange = { num2 = it.filter { c -> c.isDigit() || c == '.' } }, textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(Modifier.height(32.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).clickable {
            haptic.performIfEnabled()
            val x = num1.toDoubleOrNull() ?: 0.0
            val y = num2.toDoubleOrNull() ?: 0.0
            result = if (mode) "${String.format("%.2f", x * y / 100)}" else if (y != 0.0) "${String.format("%.2f", x / y * 100)}%" else "—"
        }, contentAlignment = Alignment.Center) {
            Text("Calculate", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (result != null) {
            Spacer(Modifier.height(24.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Result", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text(result!!, style = MaterialTheme.typography.displayMedium)
                }
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Number Base Converter ─────────────────────────────────

@Composable
private fun NumberBaseConverter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("42") }
    val dec = input.toIntOrNull() ?: 0

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Number Base", onBack)
        Spacer(Modifier.height(32.dp))
        Text("Decimal", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = input, onValueChange = { input = it.filter { c -> c.isDigit() } }, textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(Modifier.height(40.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Binary", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dec.toString(2), style = MaterialTheme.typography.bodyLarge) }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Hex", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dec.toString(16).uppercase(), style = MaterialTheme.typography.bodyLarge) }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Octal", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dec.toString(8), style = MaterialTheme.typography.bodyLarge) }
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Split Bill ────────────────────────────────────────────

@Composable
private fun SplitBill(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var total by remember { mutableStateOf(100.0) }
    var people by remember { mutableStateOf(2) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Split Bill", onBack)
        Spacer(Modifier.height(40.dp))
        Text("Total Amount", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { total = maxOf(0.0, total - 5) }
            Text("$${String.format("%.0f", total)}", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 24.dp))
            NusvMiniButton("+") { total = minOf(99999.0, total + 5) }
        }
        Spacer(Modifier.height(32.dp))
        Text("Number of People", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { people = maxOf(1, people - 1) }
            Text("$people", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 24.dp))
            NusvMiniButton("+") { people = minOf(100, people + 1) }
        }
        Spacer(Modifier.height(48.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Each Pays", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text("$${String.format("%.2f", total / people)}", style = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(8.dp))
                Text("${people} people × $${String.format("%.2f", total / people)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Date Difference ───────────────────────────────────────

@Composable
private fun DateDiffCalc(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val cal = java.util.Calendar.getInstance()
    val now = java.util.Calendar.getInstance()

    var y1 by remember { mutableStateOf(cal.get(java.util.Calendar.YEAR)) }
    var m1 by remember { mutableStateOf(cal.get(java.util.Calendar.MONTH) + 1) }
    var d1 by remember { mutableStateOf(cal.get(java.util.Calendar.DAY_OF_MONTH)) }
    var y2 by remember { mutableStateOf(cal.get(java.util.Calendar.YEAR)) }
    var m2 by remember { mutableStateOf(cal.get(java.util.Calendar.MONTH) + 1) }
    var d2 by remember { mutableStateOf(cal.get(java.util.Calendar.DAY_OF_MONTH)) }

    fun daysBetween(): Long {
        val a = java.util.Calendar.getInstance().apply { set(y1, m1 - 1, d1, 0, 0, 0); set(java.util.Calendar.MILLISECOND, 0) }
        val b = java.util.Calendar.getInstance().apply { set(y2, m2 - 1, d2, 0, 0, 0); set(java.util.Calendar.MILLISECOND, 0) }
        return kotlin.math.abs(b.timeInMillis - a.timeInMillis) / (1000 * 60 * 60 * 24)
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Date Difference", onBack)
        Spacer(Modifier.height(20.dp))
        Text("Date 1", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(4.dp))
        DateRow(y1, m1, d1, { y1 = it }, { m1 = it }, { d1 = it })
        Spacer(Modifier.height(20.dp))
        Text("Date 2", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(4.dp))
        DateRow(y2, m2, d2, { y2 = it }, { m2 = it }, { d2 = it })
        Spacer(Modifier.height(32.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${daysBetween()} days", style = MaterialTheme.typography.displayMedium)
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

@Composable
private fun DateRow(year: Int, month: Int, day: Int, onYear: (Int) -> Unit, onMonth: (Int) -> Unit, onDay: (Int) -> Unit) {
    val haptic = LocalHapticFeedback.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NusvMiniButton("+") { onYear(minOf(2099, year + 1)) }
            Text("$year", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("-") { onYear(maxOf(1900, year - 1)) }
        }
        Spacer(Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NusvMiniButton("+") { onMonth(if (month == 12) 1 else month + 1) }
            Text(String.format("%02d", month), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("-") { onMonth(if (month == 1) 12 else month - 1) }
        }
        Spacer(Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NusvMiniButton("+") { onDay(minOf(31, day + 1)) }
            Text(String.format("%02d", day), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 20.dp))
            NusvMiniButton("-") { onDay(maxOf(1, day - 1)) }
        }
    }
}

// ─── Pomodoro Timer ────────────────────────────────────────

@Composable
private fun Pomodoro(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var workSecs by remember { mutableStateOf(25 * 60) }
    var breakSecs by remember { mutableStateOf(5 * 60) }
    var remaining by remember { mutableStateOf(25 * 60) }
    var isWork by remember { mutableStateOf(true) }
    var running by remember { mutableStateOf(false) }
    var cycles by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(running) {
        while (running && remaining > 0) {
            kotlinx.coroutines.delay(1000)
            remaining--
        }
        if (remaining == 0 && running) {
            haptic.performIfEnabled()
            if (isWork) { isWork = false; remaining = breakSecs; cycles++ }
            else { isWork = true; remaining = workSecs }
        }
    }

    fun reset() { running = false; isWork = true; remaining = workSecs; cycles = 0 }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Pomodoro", onBack)
        Spacer(Modifier.weight(0.2f))
        Text(if (isWork) "Focus" else "Break", style = MaterialTheme.typography.titleLarge, color = if (isWork) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Text(String.format("%02d:%02d", remaining / 60, remaining % 60), style = MaterialTheme.typography.displayLarge)
        if (!running) {
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15 to "15m", 25 to "25m", 30 to "30m", 45 to "45m").forEach { (m, label) ->
                    Box(Modifier.background(if (workSecs == m * 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); workSecs = m * 60; remaining = m * 60; if (!isWork) { isWork = true } }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(label, style = MaterialTheme.typography.labelLarge, color = if (workSecs == m * 60) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp).background(if (running) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).clickable { haptic.performIfEnabled(); running = !running }, contentAlignment = Alignment.Center) {
            Text(if (running) "Stop" else "Start", style = MaterialTheme.typography.titleMedium, color = if (running) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)).clickable { haptic.performIfEnabled(); reset() }, contentAlignment = Alignment.Center) {
            Text("Reset", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(12.dp))
        Text("Completed: $cycles cycles", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.weight(0.2f))
    }
}

// ─── Decision Maker ────────────────────────────────────────

@Composable
private fun DecisionMaker(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Decision Maker", onBack)
        Spacer(Modifier.height(24.dp))
        Text("Enter options (comma separated)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = text, onValueChange = { text = it }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (text.isEmpty()) { Text("pizza, sushi, tacos, ramen", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).clickable {
            haptic.performIfEnabled()
            val options = text.split(",").map { it.trim() }.filter { it.isNotBlank() }
            result = if (options.isEmpty()) "Add some options" else options.random()
        }, contentAlignment = Alignment.Center) {
            Text("Pick!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (result != null) {
            Spacer(Modifier.height(24.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(result!!, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Palindrome Checker ────────────────────────────────────

@Composable
private fun PalindromeChecker(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Boolean?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Palindrome", onBack)
        Spacer(Modifier.height(32.dp))
        Text("Enter text", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = text, onValueChange = { text = it; result = null }, textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface), singleLine = true, modifier = Modifier.fillMaxWidth(), decorationBox = { inner -> if (text.isEmpty()) { Text("racecar", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(32.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).clickable {
            haptic.performIfEnabled()
            val clean = text.filter { it.isLetterOrDigit() }.lowercase()
            result = clean == clean.reversed()
        }, contentAlignment = Alignment.Center) {
            Text("Check", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (result != null) {
            Spacer(Modifier.height(32.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (result!!) "Yes! It's a palindrome" else "No, it's not", style = if (result!!) MaterialTheme.typography.displayMedium else MaterialTheme.typography.titleLarge, color = if (result!!) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Reversed: ${text.reversed()}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Todo List ─────────────────────────────────────────────

@Composable
private fun TodoList(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var items by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) }
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        AppHeader("Todo List", onBack)
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f).height(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                BasicTextField(value = input, onValueChange = { input = it }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), singleLine = true, decorationBox = { inner -> if (input.isEmpty()) { Text("Add a task...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
            }
            Spacer(Modifier.width(8.dp))
            Box(Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).clickable {
                haptic.performIfEnabled()
                if (input.isNotBlank()) { items = items + (input.trim() to false); input = "" }
            }, contentAlignment = Alignment.Center) {
                Text("+", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
        Spacer(Modifier.height(16.dp))
        if (items.isEmpty()) {
            Text("No tasks yet", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth().padding(top = 40.dp), textAlign = TextAlign.Center)
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items.forEachIndexed { i, (task, done) ->
                item {
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(24.dp).clip(CircleShape).background(if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant).clickable { haptic.performIfEnabled(); items = items.toMutableList().also { it[i] = task to !done } }, contentAlignment = Alignment.Center) {
                            if (done) Text("\u2713", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(task, style = MaterialTheme.typography.bodyLarge, color = if (done) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                        Box(Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable {
                            haptic.performIfEnabled(); items = items.toMutableList().also { it.removeAt(i) }
                        }, contentAlignment = Alignment.Center) {
                            Text("\u00D7", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("${items.size} tasks", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
    }
}

// ─── JSON Formatter ────────────────────────────────────────

@Composable
private fun JsonFormatter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("JSON Formatter", onBack)
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = input, onValueChange = { input = it; output = null; error = null }, textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (input.isEmpty()) { Text("Paste JSON here...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable {
            haptic.performIfEnabled()
            try {
                val sb = StringBuilder(); val indent = 2
                fun format(json: String, depth: Int): String {
                    val trimmed = json.trim()
                    return when {
                        trimmed.startsWith("{") || trimmed.startsWith("[") -> {
                            val out = StringBuilder()
                            out.append(if (depth > 0) "\n" else "")
                            out.append(trimmed.first())
                            var i = 1; var depth2 = depth + 1
                            val inner = trimmed.substring(1, trimmed.length - 1).trim()
                            val items = mutableListOf<String>()
                            var braceCount = 0; var inStr = false; var start = 0
                            for (j in inner.indices) {
                                val c = inner[j]
                                if (c == '"' && (j == 0 || inner[j-1] != '\\')) inStr = !inStr
                                if (!inStr) { if (c == '{' || c == '[') braceCount++; if (c == '}' || c == ']') braceCount-- }
                                if (braceCount == 0 && c == ',' && !inStr) { items.add(inner.substring(start, j).trim()); start = j + 1 }
                            }
                            if (start < inner.length) items.add(inner.substring(start).trim())
                            items.forEachIndexed { idx, item ->
                                out.append("\n"); repeat(depth2 * indent) { out.append(" ") }
                                val formatted = format(item, depth2)
                                out.append(formatted)
                                if (idx < items.size - 1) out.append(",")
                            }
                            if (items.isNotEmpty()) { out.append("\n"); repeat(depth * indent) { out.append(" ") } }
                            out.append(trimmed.last())
                            if (depth > 0) out.toString() else out.toString()
                        }
                        else -> trimmed
                    }
                }
                val parsed = org.json.JSONObject(input)
                output = parsed.toString(2)
                error = null
            } catch (e: org.json.JSONException) {
                try {
                    val parsed = org.json.JSONArray(input)
                    output = parsed.toString(2)
                    error = null
                } catch (e2: Exception) {
                    output = null
                    error = "Invalid JSON: ${e2.message}"
                }
            }
        }, contentAlignment = Alignment.Center) {
            Text("Format", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (error != null) { Spacer(Modifier.height(12.dp)); Text(error!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth()) }
        if (output != null) {
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp).verticalScroll(rememberScrollState())) {
                Text(output!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }
        }
        Spacer(Modifier.weight(0.1f))
    }
}

// ─── Base64 Tool ───────────────────────────────────────────

@Composable
private fun Base64Tool(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var encode by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Base64 Tool", onBack)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.background(if (encode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); encode = true }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("Encode", style = MaterialTheme.typography.labelLarge, color = if (encode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(Modifier.background(if (!encode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); encode = false }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("Decode", style = MaterialTheme.typography.labelLarge, color = if (!encode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = input, onValueChange = { input = it; error = false }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (input.isEmpty()) { Text(if (encode) "Text to encode..." else "Base64 to decode...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable {
            haptic.performIfEnabled()
            try {
                output = if (encode) android.util.Base64.encodeToString(input.toByteArray(), android.util.Base64.DEFAULT).trim()
                else String(android.util.Base64.decode(input.trim(), android.util.Base64.DEFAULT))
                error = false
            } catch (e: Exception) { error = true; output = "" }
        }, contentAlignment = Alignment.Center) {
            Text(if (encode) "Encode" else "Decode", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (output.isNotEmpty() && !error) { Spacer(Modifier.height(12.dp)); Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) { Text(output, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface) } }
        if (error) { Spacer(Modifier.height(8.dp)); Text("Invalid Base64 input", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Color Converter ───────────────────────────────────────

@Composable
private fun ColorConverter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var hex by remember { mutableStateOf("#FF2D78") }
    var r by remember { mutableStateOf(255) }; var g by remember { mutableStateOf(45) }; var b by remember { mutableStateOf(120) }

    fun updateFromHex(h: String) { try { val c = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(h)); r = (c.red * 255).toInt(); g = (c.green * 255).toInt(); b = (c.blue * 255).toInt() } catch (_: Exception) {} }
    fun updateFromRgb() { hex = String.format("#%02X%02X%02X", r.coerceIn(0,255), g.coerceIn(0,255), b.coerceIn(0,255)) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Color Converter", onBack)
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth().height(80.dp).background(androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(hex)), RoundedCornerShape(12.dp)))
        Spacer(Modifier.height(20.dp))
        Text("HEX", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = hex, onValueChange = { hex = it; if (it.length == 7) updateFromHex(it) }, textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))
        Text("RGB", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("R", color = MaterialTheme.colorScheme.onSurfaceVariant); NusvMiniButton("+") { r = (r + 5).coerceAtMost(255); updateFromRgb() }; Text("$r", style = MaterialTheme.typography.bodyLarge); NusvMiniButton("-") { r = (r - 5).coerceAtLeast(0); updateFromRgb() } }
            Spacer(Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("G", color = MaterialTheme.colorScheme.onSurfaceVariant); NusvMiniButton("+") { g = (g + 5).coerceAtMost(255); updateFromRgb() }; Text("$g", style = MaterialTheme.typography.bodyLarge); NusvMiniButton("-") { g = (g - 5).coerceAtLeast(0); updateFromRgb() } }
            Spacer(Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("B", color = MaterialTheme.colorScheme.onSurfaceVariant); NusvMiniButton("+") { b = (b + 5).coerceAtMost(255); updateFromRgb() }; Text("$b", style = MaterialTheme.typography.bodyLarge); NusvMiniButton("-") { b = (b - 5).coerceAtLeast(0); updateFromRgb() } }
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── UUID Generator ────────────────────────────────────────

@Composable
private fun UuidGenerator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
    var uuids by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("UUID Generator", onBack)
        Spacer(Modifier.weight(0.2f))
        if (uuids.isEmpty()) {
            Text("Tap Generate to create UUIDs", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                uuids.forEachIndexed { i, uuid ->
                    item { Text(uuid, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 6.dp).clickable { clipboard.setText(androidx.compose.ui.text.AnnotatedString(uuid)); haptic.performIfEnabled() }) }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(48.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).clickable { haptic.performIfEnabled(); uuids = listOf(java.util.UUID.randomUUID().toString()) + uuids }, contentAlignment = Alignment.Center) {
            Text("Generate", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(12.dp))
        if (uuids.isNotEmpty()) {
            Box(Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)).clickable { haptic.performIfEnabled(); uuids = emptyList() }, contentAlignment = Alignment.Center) {
                Text("Clear", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.weight(0.2f))
    }
}

// ─── Hash Generator ────────────────────────────────────────

@Composable
private fun HashGenerator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("") }
    var md5 by remember { mutableStateOf("") }; var sha1 by remember { mutableStateOf("") }; var sha256 by remember { mutableStateOf("") }

    fun compute() {
        try {
            val bytes = input.toByteArray()
            md5 = java.security.MessageDigest.getInstance("MD5").digest(bytes).joinToString("") { "%02x".format(it) }
            sha1 = java.security.MessageDigest.getInstance("SHA-1").digest(bytes).joinToString("") { "%02x".format(it) }
            sha256 = java.security.MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        } catch (_: Exception) {}
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        AppHeader("Hash Generator", onBack)
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(80.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = input, onValueChange = { input = it; compute() }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (input.isEmpty()) { Text("Type to hash...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        if (input.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            GlassCard(Modifier.fillMaxWidth()) { Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("MD5", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp)); Text(md5, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(10.dp))
                Text("SHA-1", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp)); Text(sha1, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(10.dp))
                Text("SHA-256", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 2.dp)); Text(sha256, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }}
        }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Epoch Converter ───────────────────────────────────────

@Composable
private fun EpochConverter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var epoch by remember { mutableStateOf((System.currentTimeMillis() / 1000).toString()) }
    var dateText by remember { mutableStateOf("") }
    var localText by remember { mutableStateOf("") }

    fun convert() {
        try {
            val secs = epoch.toLongOrNull() ?: 0
            val date = java.util.Date(secs * 1000)
            dateText = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(date)
            localText = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'", java.util.Locale.getDefault()).format(java.util.Date(secs * 1000 - java.util.TimeZone.getDefault().getOffset(secs * 1000)))
        } catch (_: Exception) { dateText = ""; localText = "" }
    }

    LaunchedEffect(Unit) { convert() }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Epoch Converter", onBack)
        Spacer(Modifier.height(32.dp))
        Text("Unix Timestamp (seconds)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = epoch, onValueChange = { epoch = it.filter { c -> c.isDigit() }; convert() }, textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(Modifier.height(32.dp))
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable { haptic.performIfEnabled(); val now = (System.currentTimeMillis() / 1000).toString(); epoch = now; convert() }.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
            Text("Now", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(32.dp))
        GlassCard(Modifier.fillMaxWidth()) { Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Local Time", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(4.dp)); Text(dateText, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            Text("UTC", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(4.dp)); Text(localText, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }}
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── URL Tool ──────────────────────────────────────────────

@Composable
private fun UrlTool(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var encode by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("URL Tool", onBack)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.background(if (encode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); encode = true }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("Encode", style = MaterialTheme.typography.labelLarge, color = if (encode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(Modifier.background(if (!encode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); encode = false }.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("Decode", style = MaterialTheme.typography.labelLarge, color = if (!encode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(100.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = input, onValueChange = { input = it }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (input.isEmpty()) { Text(if (encode) "URL to encode..." else "Encoded URL...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable {
            haptic.performIfEnabled()
            try { output = if (encode) java.net.URLEncoder.encode(input, "UTF-8") else java.net.URLDecoder.decode(input, "UTF-8") } catch (_: Exception) { output = "" }
        }, contentAlignment = Alignment.Center) {
            Text("Convert", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (output.isNotEmpty()) { Spacer(Modifier.height(12.dp)); Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) { Text(output, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface) } }
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Case Converter ────────────────────────────────────────

@Composable
private fun CaseConverter(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var input by remember { mutableStateOf("hello world") }

    val camel = input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }.joinToString("") { it.replaceFirstChar { c -> if (it == input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }.first()) c.lowercase() else c.uppercase() } }
    val pascal = input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }.joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
    val snake = input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }.joinToString("_") { it.lowercase() }
    val kebab = input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }.joinToString("-") { it.lowercase() }
    val upper = input.uppercase()
    val lower = input.lowercase()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        AppHeader("Case Converter", onBack)
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(80.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = input, onValueChange = { input = it }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (input.isEmpty()) { Text("Type here...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(16.dp))
        GlassCard(Modifier.fillMaxWidth()) { Column(Modifier.fillMaxWidth().padding(16.dp)) {
            listOf("camelCase" to camel, "PascalCase" to pascal, "snake_case" to snake, "kebab-case" to kebab, "UPPER" to upper, "lower" to lower).forEach { (label, value) ->
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 2.dp))
                Text(value.ifEmpty { "—" }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(6.dp))
            }
        }}
        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Regex Tester ──────────────────────────────────────────

@Composable
private fun RegexTester(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var pattern by remember { mutableStateOf("") }
    var testText by remember { mutableStateOf("") }
    var matches by remember { mutableStateOf<List<String>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Regex Tester", onBack)
        Spacer(Modifier.height(16.dp))
        Text("Pattern", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        BasicTextField(value = pattern, onValueChange = { pattern = it }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), singleLine = true, modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(12.dp), decorationBox = { inner -> if (pattern.isEmpty()) { Text("[A-Z]+\\\\d+", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        Spacer(Modifier.height(16.dp))
        Text("Test Text", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Box(Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp)) {
            BasicTextField(value = testText, onValueChange = { testText = it }, textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxSize(), decorationBox = { inner -> if (testText.isEmpty()) { Text("ABC123 def 456", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }; inner() })
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable {
            haptic.performIfEnabled()
            try { val re = Regex(pattern); matches = re.findAll(testText).map { it.value }.toList(); error = null } catch (e: Exception) { error = e.message; matches = emptyList() }
        }, contentAlignment = Alignment.Center) {
            Text("Test", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        if (error != null) { Spacer(Modifier.height(8.dp)); Text("Error: $error", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }
        if (matches.isNotEmpty()) { Spacer(Modifier.height(12.dp)); GlassCard(Modifier.fillMaxWidth()) { Column(Modifier.fillMaxWidth().padding(16.dp)) { Text("${matches.size} match(es)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); matches.forEach { Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp)) } } } }
        Spacer(Modifier.weight(0.1f))
    }
}

// ─── Lorem Ipsum ───────────────────────────────────────────

@Composable
private fun LoremIpsum(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    var paragraphs by remember { mutableStateOf(1) }
    var output by remember { mutableStateOf(lorem) }
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        AppHeader("Lorem Ipsum", onBack)
        Spacer(Modifier.height(24.dp))
        Text("Paragraphs", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            NusvMiniButton("-") { paragraphs = maxOf(1, paragraphs - 1); output = (1..paragraphs).joinToString("\n\n") { lorem } }
            Text("$paragraphs", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 24.dp))
            NusvMiniButton("+") { paragraphs = minOf(10, paragraphs + 1); output = (1..paragraphs).joinToString("\n\n") { lorem } }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(250.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).padding(12.dp).verticalScroll(rememberScrollState())) {
            Text(output, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(44.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable { haptic.performIfEnabled(); clipboard.setText(androidx.compose.ui.text.AnnotatedString(output)) }, contentAlignment = Alignment.Center) {
            Text("Copy", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.weight(0.1f))
    }
}

// ─── Password Generator ───────────────────────────────────

@Composable
private fun PasswordGenerator(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()_+-=[]{}|;:,.<>?"
    var length by remember { mutableStateOf(16) }
    var includeSpecial by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Password Generator", onBack)
        Spacer(Modifier.weight(0.2f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = password,
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                },
                label = "passwordDisplay"
            ) { pw ->
                Text(
                    text = pw ?: "Tap Generate",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (pw != null) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Length: $length",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            listOf(8, 12, 16, 20, 24).forEach { n ->
                Box(
                    modifier = Modifier
                        .background(
                            if (length == n) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { haptic.performIfEnabled(); length = n }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$n",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (length == n) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        if (includeSpecial) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(4.dp)
                    )
                    .clickable { haptic.performIfEnabled(); includeSpecial = !includeSpecial },
                contentAlignment = Alignment.Center
            ) {
                if (includeSpecial) {
                    Text(
                        text = "\u2713",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = "Include special characters (!@#\$%...)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clickable {
                    haptic.performIfEnabled()
                    val pool = if (includeSpecial) chars else chars.take(62)
                    password = (1..length).map { pool.random() }.joinToString("")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Generate",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.weight(0.3f))
    }
}

// ─── Breathing Exercise ───────────────────────────────────

@Composable
private fun BreathingExercise(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var phase by remember { mutableStateOf("Inhale") }
    var countdown by remember { mutableStateOf(4) }
    var running by remember { mutableStateOf(false) }

    val phases = listOf(
        "Inhale" to 4,
        "Hold" to 4,
        "Exhale" to 4,
        "Hold" to 4
    )
    var phaseIndex by remember { mutableStateOf(0) }

    LaunchedEffect(running, phaseIndex) {
        if (!running) return@LaunchedEffect
        val (_, seconds) = phases[phaseIndex]
        countdown = seconds
        while (countdown > 0) {
            kotlinx.coroutines.delay(1000)
            countdown--
        }
        phaseIndex = (phaseIndex + 1) % phases.size
        phase = phases[phaseIndex].first
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Breathing", onBack)
        Spacer(Modifier.weight(0.25f))

        val targetSize = when (phase) {
            "Inhale" -> 220.dp
            "Exhale" -> 160.dp
            else -> 200.dp
        }
        val animSize by animateDpAsState(targetSize, label = "breath")

        val targetAlpha = when (phase) {
            "Inhale" -> 0.20f
            "Exhale" -> 0.06f
            else -> 0.12f
        }
        val animAlpha by animateFloatAsState(targetAlpha, label = "alpha")

        Box(
            modifier = Modifier
                .size(animSize)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = animAlpha),
                    RoundedCornerShape(animSize / 2)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = phase,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                if (running) {
                    Text(
                        text = "$countdown",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "4-4-4-4 box breathing",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
                .background(
                    if (running) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
                .clickable {
                    haptic.performIfEnabled()
                    running = !running
                    if (!running) {
                        phase = "Inhale"
                        countdown = 4
                        phaseIndex = 0
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (running) "Stop" else "Start",
                style = MaterialTheme.typography.titleMedium,
                color = if (running) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.weight(0.25f))
    }
}

// ─── Slot Machine ─────────────────────────────────────────

private val slotSymbols = listOf("7", "CH", "DM", "BL", "ST", "LK", "BR")

@Composable
private fun SlotMachine(onBack: () -> Unit) {
    var reels by remember { mutableStateOf(listOf("?", "?", "?")) }
    var spinning by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Pull the lever!") }
    var credits by remember { mutableStateOf(100) }
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader("Slot Machine", onBack)
        Spacer(Modifier.weight(0.1f))

        Text(
            text = "Credits: $credits",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            reels.forEachIndexed { i, symbol ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = symbol,
                        style = MaterialTheme.typography.displayLarge,
                        color = if (symbol == "7") MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = when {
                message.contains("WIN") -> MaterialTheme.colorScheme.primary
                message == "Jackpot!" -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(56.dp)
                .background(
                    if (spinning || credits <= 0) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
                .clickable(enabled = !spinning && credits >= 10) {
                    haptic.performIfEnabled()
                    spinning = true
                    credits -= 10
                    message = "..."
                    val final = (1..3).map { slotSymbols.random() }
                    val steps = buildList {
                        repeat(8) { add((1..3).map { slotSymbols.random() }) }
                        add(final)
                    }
                    var idx = 0
                    scope.launch {
                        while (idx < steps.size) {
                            reels = steps[idx]
                            idx++
                            kotlinx.coroutines.delay(80)
                        }
                        spinning = false
                        val result = final
                        message = when {
                            result.distinct().size == 1 -> {
                                credits += 100
                                if (result[0] == "7") { credits += 200; "JACKPOT! +300" }
                                else "WIN! +100"
                            }
                            result[0] == result[1] || result[1] == result[2] || result[0] == result[2] -> {
                                credits += 20
                                "PAIR! +20"
                            }
                            else -> "No luck..."
                        }
                        haptic.performIfEnabled()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (spinning) "Spinning..." else if (credits < 10) "Out of credits" else "SPIN (10 credits)",
                style = MaterialTheme.typography.titleMedium,
                color = if (spinning || credits < 10) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onPrimary
            )
        }

        if (credits < 10 && !spinning) {
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(44.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .clickable {
                        haptic.performIfEnabled()
                        credits = 100
                        message = "Pull the lever!"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Refill (100)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.weight(0.3f))
    }
}


// ─── Flashlight ─────────────────────────────────────────

@Composable
private fun Flashlight(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var isOn by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isOn) Color.White else MaterialTheme.colorScheme.background)
            .clickable { haptic.performIfEnabled(); isOn = !isOn },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!isOn) {
                Text("💡", fontSize = 48.sp)
                Spacer(Modifier.height(16.dp))
                Text("Tap to turn on", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .clickable { haptic.performIfEnabled(); onBack() }
                        .padding(horizontal = 32.dp, vertical = 10.dp),
                ) { Text("← Back", color = MaterialTheme.colorScheme.onPrimary) }
            } else {
                Text("☀️", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("Tap to turn off", style = MaterialTheme.typography.bodyLarge, color = Color(0xFF444444))
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0x88CCCCCC), RoundedCornerShape(12.dp))
                        .clickable { haptic.performIfEnabled(); onBack() }
                        .padding(horizontal = 32.dp, vertical = 10.dp),
                ) { Text("← Back", color = Color(0xFF444444)) }
            }
        }
    }
}

// ─── Battery Info ────────────────────────────────────────

@Composable
private fun BatteryInfo(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val ctx = LocalContext.current
    var level by remember { mutableIntStateOf(0) }
    var isCharging by remember { mutableStateOf(false) }
    var temperature by remember { mutableIntStateOf(0) }
    var voltage by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        val bm = ctx.getSystemService(android.content.Context.BATTERY_SERVICE) as? BatteryManager
        if (bm != null) {
            level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                temperature = bm.getIntProperty(3) / 10
                voltage = bm.getIntProperty(4)
            }
            isCharging = bm.isCharging
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))
        Box(
            modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() },
            contentAlignment = Alignment.Center,
        ) { Text("←", style = MaterialTheme.typography.titleLarge) }
        Spacer(Modifier.height(24.dp))
        Text("🔋", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text("$level%", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        Text(if (isCharging) "⚡ Charging" else "Not charging", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🌡️ Temperature", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${temperature}°C", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("⚡ Voltage", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${voltage}mV", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─── Quick Timer ─────────────────────────────────────────

@Composable
private fun QuickTimer(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    var remaining by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    var preset by remember { mutableIntStateOf(60) }
    LaunchedEffect(running) {
        if (!running) return@LaunchedEffect
        while (remaining > 0) { delay(1000); remaining-- }
        running = false
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))
        Box(
            modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).clickable { haptic.performIfEnabled(); onBack() },
            contentAlignment = Alignment.Center,
        ) { Text("←", style = MaterialTheme.typography.titleLarge) }
        Spacer(Modifier.height(24.dp))
        Text("⏱️", fontSize = 48.sp)
        Spacer(Modifier.height(8.dp))
        Text("%d:%02d".format(remaining / 60, remaining % 60), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(60 to "1m", 180 to "3m", 300 to "5m", 600 to "10m").forEach { (sec, label) ->
                Box(
                    modifier = Modifier
                        .background(if (!running && preset == sec) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .clickable(enabled = !running) { haptic.performIfEnabled(); preset = sec; remaining = sec }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) { Text(label, color = if (!running && preset == sec) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold) }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .background(if (running) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .clickable(enabled = remaining > 0) { haptic.performIfEnabled(); running = !running }
                    .padding(horizontal = 32.dp, vertical = 12.dp),
            ) { Text(if (running) "Pause" else "Start", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold) }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .clickable { haptic.performIfEnabled(); running = false; remaining = preset }
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) { Text("Reset", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold) }
        }
    }
}




// ─── Matrix Rain (Orca exclusive) ─────────────────────

@Composable
private fun MatrixRain(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val chars = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン0123456789"
    val columns = 20
    var lines by remember { mutableStateOf(List(columns) { "" }) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(150)
            lines = lines.mapIndexed { col, s ->
                val newChar = chars[(0..chars.length-1).random()]
                if (s.length > 20) newChar.toString()
                else newChar + s
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).clickable { haptic.performIfEnabled(); onBack() },
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.align(Alignment.TopStart).padding(12.dp)) {
            Text("\u2190 Back", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.labelSmall)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("\uD83D\uDDA4", fontSize = 36.sp)
            Text("Matrix Rain", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF00FF00), fontWeight = FontWeight.Bold)
        }
        lines.forEachIndexed { col, line ->
            Box(Modifier.align(Alignment.TopStart).padding(start = (8 + col * 18).dp, top = 60.dp)) {
                Text(
                    text = line.take(1),
                    color = Color(0xFF00FF00).copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

// ─── Secret Vault (Orca exclusive) ──────────────────

@Composable
private fun SecretVault(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val ctx = LocalContext.current
    val prefs = remember { ctx.getSharedPreferences("orca_vault", android.content.Context.MODE_PRIVATE) }
    var unlocked by remember { mutableStateOf(false) }
    var storedPin by remember { mutableStateOf(prefs.getString("pin", "") ?: "") }
    var notes by remember { mutableStateOf(prefs.getString("notes", "") ?: "") }
    var pinInput by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier.size(36.dp).background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .clickable { haptic.performIfEnabled(); onBack() },
            contentAlignment = Alignment.Center,
        ) { Text("\u2190", color = Color.White, style = MaterialTheme.typography.titleLarge) }
        Spacer(Modifier.height(16.dp))
        Text("\uD83D\uDD12", fontSize = 48.sp)
        Spacer(Modifier.height(8.dp))
        Text("Secret Vault", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)

        Spacer(Modifier.height(24.dp))

        if (!unlocked) {
            Text(if (storedPin.isEmpty()) "Set your PIN" else "Enter PIN", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.6f))
            Spacer(Modifier.height(8.dp))
            androidx.compose.foundation.text.BasicTextField(
                value = pinInput,
                onValueChange = { if (it.length <= 6) pinInput = it },
                modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp)).padding(16.dp),
                textStyle = MaterialTheme.typography.headlineMedium.copy(color = Color.White, textAlign = TextAlign.Center, letterSpacing = 8.sp),
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword),
                singleLine = true,
                decorationBox = { inner ->
                    if (pinInput.isEmpty()) Text("****", color = Color.White.copy(alpha = 0.2f), style = MaterialTheme.typography.headlineLarge)
                    inner()
                }
            )
            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color(0xFFFF6B6B), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    haptic.performIfEnabled()
                    if (storedPin.isEmpty()) {
                        if (pinInput.length >= 4) {
                            storedPin = pinInput
                            prefs.edit().putString("pin", pinInput).apply()
                            unlocked = true
                            errorMsg = ""
                        } else {
                            errorMsg = "PIN must be at least 4 digits"
                        }
                    } else if (pinInput == storedPin) {
                        unlocked = true
                        errorMsg = ""
                    } else {
                        errorMsg = "Wrong PIN!"
                        pinInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) { Text(if (storedPin.isEmpty()) "Set PIN" else "Unlock", fontWeight = FontWeight.Bold) }
        } else {
            androidx.compose.foundation.text.BasicTextField(
                value = notes,
                onValueChange = {
                    notes = it
                    prefs.edit().putString("notes", it).apply()
                },
                modifier = Modifier.fillMaxWidth().weight(1f).background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(12.dp)).padding(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                decorationBox = { inner ->
                    if (notes.isEmpty()) Text("Write your secret notes here...", color = Color.White.copy(alpha = 0.3f), style = MaterialTheme.typography.bodyLarge)
                    inner()
                }
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    haptic.performIfEnabled()
                    unlocked = false
                    pinInput = ""
                    prefs.edit().putString("notes", notes).apply()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f), contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
            ) { Text("Lock", fontWeight = FontWeight.Bold) }
        }
    }
}
