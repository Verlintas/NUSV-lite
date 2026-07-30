package com.nusv.lite.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusv.lite.ui.components.GlassCard
import com.nusv.lite.util.performIfEnabled
import kotlinx.coroutines.delay

private data class Quote(val text: String, val author: String, val category: String)

private val quotes = listOf(
    Quote("The only way to do great work is to love what you do.", "Steve Jobs", "Inspirational"),
    Quote("Be the change you wish to see in the world.", "Mahatma Gandhi", "Inspirational"),
    Quote("Stay hungry, stay foolish.", "Steve Jobs", "Inspirational"),
    Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", "Inspirational"),
    Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius", "Inspirational"),
    Quote("Believe you can and you're halfway there.", "Theodore Roosevelt", "Inspirational"),
    Quote("The only impossible journey is the one you never begin.", "Tony Robbins", "Inspirational"),
    Quote("Everything you've ever wanted is on the other side of fear.", "George Addair", "Inspirational"),
    Quote("The best time to plant a tree was 20 years ago. The second best time is now.", "Chinese Proverb", "Inspirational"),
    Quote("What lies behind us and what lies before us are tiny matters compared to what lies within us.", "Ralph Waldo Emerson", "Inspirational"),
    Quote("Dream big and dare to fail.", "Norman Vaughan", "Inspirational"),
    Quote("The secret of getting ahead is getting started.", "Mark Twain", "Inspirational"),
    Quote("The only true wisdom is in knowing you know nothing.", "Socrates", "Wisdom"),
    Quote("The unexamined life is not worth living.", "Socrates", "Wisdom"),
    Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", "Wisdom"),
    Quote("Knowing yourself is the beginning of all wisdom.", "Aristotle", "Wisdom"),
    Quote("It is the mark of an educated mind to be able to entertain a thought without accepting it.", "Aristotle", "Wisdom"),
    Quote("Simplicity is the ultimate sophistication.", "Leonardo da Vinci", "Wisdom"),
    Quote("The journey of a thousand miles begins with one step.", "Lao Tzu", "Wisdom"),
    Quote("He who knows all the answers has not been asked all the questions.", "Confucius", "Wisdom"),
    Quote("Silence is a source of great strength.", "Lao Tzu", "Wisdom"),
    Quote("Choose a job you love, and you will never have to work a day in your life.", "Confucius", "Wisdom"),
    Quote("The wise speak only of what they know.", "J.R.R. Tolkien", "Wisdom"),
    Quote("Patience is not the ability to wait, but the ability to keep a good attitude while waiting.", "Unknown", "Wisdom"),
    Quote("I'm not arguing, I'm just explaining why I'm right.", "Unknown", "Humor"),
    Quote("I would agree with you but then we'd both be wrong.", "Unknown", "Humor"),
    Quote("My favorite machine at the gym is the vending machine.", "Unknown", "Humor"),
    Quote("I'm on a whiskey diet. I've lost three days already.", "Tommy Cooper", "Humor"),
    Quote("I'm not lazy, I'm on energy-saving mode.", "Unknown", "Humor"),
    Quote("Age is of no importance unless you're a cheese.", "Luis Buñuel", "Humor"),
    Quote("I told my wife she should embrace her mistakes. She gave me a hug.", "Unknown", "Humor"),
    Quote("The road to success is dotted with many tempting parking spaces.", "Will Rogers", "Humor"),
    Quote("I'm reading a book on anti-gravity. It's impossible to put down.", "Unknown", "Humor"),
    Quote("I used to be indecisive. Now I'm not so sure.", "Unknown", "Humor"),
    Quote("If at first you don't succeed, skydiving is not for you.", "Unknown", "Humor"),
    Quote("Better to remain silent and be thought a fool than to speak out and remove all doubt.", "Abraham Lincoln", "Humor"),
    Quote("Life is what happens when you're busy making other plans.", "John Lennon", "Life"),
    Quote("Get busy living or get busy dying.", "Stephen King", "Life"),
    Quote("The purpose of our lives is to be happy.", "Dalai Lama", "Life"),
    Quote("In three words I can sum up everything I've learned about life: it goes on.", "Robert Frost", "Life"),
    Quote("Life is really simple, but we insist on making it complicated.", "Confucius", "Life"),
    Quote("The only thing we have to fear is fear itself.", "Franklin D. Roosevelt", "Life"),
    Quote("Good friends, good books, and a sleepy conscience: this is the ideal life.", "Mark Twain", "Life"),
    Quote("Life isn't about finding yourself. Life is about creating yourself.", "George Bernard Shaw", "Life"),
    Quote("Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment.", "Buddha", "Life"),
    Quote("Happiness is not something ready made. It comes from your own actions.", "Dalai Lama", "Life"),
    Quote("To live is the rarest thing in the world. Most people exist, that is all.", "Oscar Wilde", "Life"),
    Quote("Life is either a daring adventure or nothing at all.", "Helen Keller", "Life"),
    Quote("Any sufficiently advanced technology is indistinguishable from magic.", "Arthur C. Clarke", "Tech"),
    Quote("The best way to predict the future is to invent it.", "Alan Kay", "Tech"),
    Quote("Software is a great combination of artistry and engineering.", "Bill Gates", "Tech"),
    Quote("First, solve the problem. Then, write the code.", "John Johnson", "Tech"),
    Quote("Talk is cheap. Show me the code.", "Linus Torvalds", "Tech"),
    Quote("Programs must be written for people to read, and only incidentally for machines to execute.", "Harold Abelson", "Tech"),
    Quote("The computer was born to solve problems that did not exist before.", "Bill Gates", "Tech"),
    Quote("Debugging is twice as hard as writing the code in the first place.", "Brian Kernighan", "Tech"),
    Quote("Technology is best when it brings people together.", "Matt Mullenweg", "Tech"),
    Quote("It's not that I'm so smart, it's just that I stay with problems longer.", "Albert Einstein", "Tech"),
    Quote("The Web as I envisaged it, we have not seen it yet. The future is still so much bigger than the past.", "Tim Berners-Lee", "Tech"),
    Quote("The most dangerous phrase in the language is: 'We've always done it this way.'", "Grace Hopper", "Tech"),
)

private val categories = listOf("All", "Inspirational", "Wisdom", "Humor", "Life", "Tech")

@Composable
fun RandomQuotes(onBack: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val filtered = remember {
        mutableStateOf(quotes)
    }
    var selectedCategory by remember { mutableStateOf("All") }
    var currentQuote by remember { mutableStateOf(quotes.random()) }
    var showCopied by remember { mutableStateOf(false) }

    LaunchedEffect(showCopied) {
        if (showCopied) {
            delay(1500)
            showCopied = false
        }
    }

    fun pickRandom() {
        val pool = filtered.value
        if (pool.isEmpty()) return
        currentQuote = pool.random()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            Text("Random Quotes", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedCategory
                val chipBg = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
                val chipText = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(chipBg)
                        .clickable {
                            haptic.performIfEnabled()
                            selectedCategory = category
                            filtered.value = if (category == "All") quotes
                            else quotes.filter { it.category == category }
                            pickRandom()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(category, style = MaterialTheme.typography.labelLarge, color = chipText)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = currentQuote,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300)) using
                                SizeTransform(clip = false)
                    },
                ) { quote ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "\u201C",
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = quote.text,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 36.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "\u2014 ${quote.author}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        haptic.performIfEnabled()
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("quote", "\u201C${currentQuote.text}\u201D \u2014 ${currentQuote.author}")
                        clipboard.setPrimaryClip(clip)
                        showCopied = true
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (showCopied) "\u2713 Copied!" else "Copy",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (showCopied) Color(0xFF4CAF50)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .clickable {
                        haptic.performIfEnabled()
                        val shareText = "\u201C${currentQuote.text}\u201D \u2014 ${currentQuote.author}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, null))
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Share",
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
                        pickRandom()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Next \u2192",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
