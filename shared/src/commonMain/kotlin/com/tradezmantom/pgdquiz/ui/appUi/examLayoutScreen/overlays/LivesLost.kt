package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.ui.appUi.BannerAdPlacement
import com.tradezmantom.pgdquiz.ui.appUi.PlatformBannerAd
import com.tradezmantom.pgdquiz.ui.data.QuizType
import org.jetbrains.compose.resources.painterResource
import pgdquiz.shared.generated.resources.Res
import pgdquiz.shared.generated.resources.arrow
import pgdquiz.shared.generated.resources.drop2
import pgdquiz.shared.generated.resources.happypoo2
import pgdquiz.shared.generated.resources.pressure2

@Composable
fun LivesLost(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    examEmoji: Painter,
    emojiCont: String,
    onWatchAd: (() -> Unit)?,
    watchAdEnabled: Boolean = true,
    showAds: Boolean = true,
    onExit: () -> Unit,
    onUpgrade: () -> Unit = {},
    onUpgradeApprentice: () -> Unit = {},
    onUpgradeTradesman: () -> Unit = {},
    apprenticePriceText: String = "NZD$10/week",
    tradesmanPriceText: String = "NZD$300 Lifetime"
) {
    val (title, examEmoji) = when (quizType) {
        QuizType.DRAIN_LAYING -> "HappyPoo" to Res.drawable.happypoo2
        QuizType.PLUMBING -> "Droplet" to Res.drawable.drop2
        QuizType.GASFITTING -> "Pressure" to Res.drawable.pressure2
        QuizType.DEFAULT -> "Nothing G" to Res.drawable.arrow
    }

    val drainlayingMessages = listOf(
        "Got laid out", "Do you even lay?", "That's a Box",
        "Bungs in the wrong place cuz", "Well that'll fail inspection",
        "The Plumber Could Do It..", "Its Backfalling!",
        "Down the drain, huh?", "That one flushed your streak!",
        "Drain again next time!"
    )
    val plumbingMessages = listOf(
        "You sprung a leak!", "Thats a Box", "Pressure Dropped",
        "I wont tell the boss", "Gassfitters laughing bro",
        "Well that'll fail inspection", "You forgot a Crimp..",
        "No better then a Drainlayer..", "Pipe dream ended there.",
        "Try plunging into it again!"
    )
    val gasfittingMessages = listOf(
        "Gas leak detected... in your answers!", "I wont tell the boss",
        "Thats a Box", "Atmospheric Pressure maybe?", "Pressure dropped..",
        "Should have been a Plumber..", "Get the leak check..",
        "You sure you tightend that fitting?", "Better ventilate your thinking!"
    )
    val messages = when (quizType) {
        QuizType.DRAIN_LAYING -> drainlayingMessages
        QuizType.PLUMBING -> plumbingMessages
        QuizType.GASFITTING -> gasfittingMessages
        else -> listOf("Oops, try again!")
    }

    val message = remember(quizType) { messages.random() }
    val glowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background
        )
    )

    val adActionColor = Color(0xFF1A8FBF)
    val adActionColorDark = Color(0xFF106487)

    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradientBrush)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.outline
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = message,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.White,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
            )
            Text(
                text = "You're out of lives!",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f)
            )

            Image(
                painter = painterResource(examEmoji),
                contentDescription = emojiCont,
                modifier = Modifier.size(80.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onWatchAd != null) {
                    val effectiveAdColor = if (watchAdEnabled) adActionColor else adActionColor.copy(alpha = 0.4f)
                    val effectiveAdColorDark = if (watchAdEnabled) adActionColorDark else adActionColorDark.copy(alpha = 0.4f)
                    Button(
                        onClick = onWatchAd,
                        enabled = watchAdEnabled,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp), ambientColor = effectiveAdColor.copy(alpha = 0.5f), spotColor = effectiveAdColor.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(effectiveAdColor, effectiveAdColorDark)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.5.dp, effectiveAdColorDark, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    if (watchAdEnabled) "Watch Ad for +1 Attempt" else "Loading Ad…",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onExit,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Leave Quiz", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Or upgrade for unlimited attempts",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onUpgradeApprentice,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\u26A1 Apprentice — $apprenticePriceText",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = onUpgradeTradesman,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\uD83D\uDD28 Tradesman — $tradesmanPriceText",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (showAds) {
                PlatformBannerAd(
                    placement = BannerAdPlacement.LIVES_LOST,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}