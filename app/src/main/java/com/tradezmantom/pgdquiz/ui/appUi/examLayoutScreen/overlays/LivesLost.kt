package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.R
import com.tradezmantom.pgdquiz.ui.data.QuizType

@Composable
fun LivesLost(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    examEmoji: Painter,
    emojiCont: String,
    onWatchAd: (() -> Unit)?,
    onExit: () -> Unit,
    onUpgrade: () -> Unit = {},
    onUpgradeApprentice: () -> Unit = {},
    onUpgradeTradesman: () -> Unit = {}
) {
    val (title, examEmoji) = when (quizType) {
        QuizType.DRAIN_LAYING -> "HappyPoo" to R.drawable.happypoo2
        QuizType.PLUMBING -> "Droplet" to R.drawable.drop2
        QuizType.GASFITTING -> "Pressure" to R.drawable.pressure2
        QuizType.DEFAULT -> "Nothing G" to R.drawable.arrow
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

    Box(
        modifier = Modifier
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
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.White,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
            )

            Image(
                painter = painterResource(id = examEmoji),
                contentDescription = emojiCont,
                modifier = Modifier.size(80.dp)
            )

            // Action buttons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                if (onWatchAd != null) {
                    Button(
                        onClick = onWatchAd,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp), ambientColor = glowColor, spotColor = glowColor)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.outline
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Gain 1 attempt", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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

            // Upgrade buttons
            Button(
                onClick = onUpgradeApprentice,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp)
                    .shadow(6.dp, RoundedCornerShape(12.dp), ambientColor = glowColor, spotColor = glowColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.outline
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚡ Apprentice — $25/week", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }

            Button(
                onClick = onUpgradeTradesman,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp, vertical = 0.dp)
                    .padding(bottom = 4.dp)
                    .shadow(6.dp, RoundedCornerShape(12.dp), ambientColor = glowColor, spotColor = glowColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.colorScheme.primary
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔨 Tradesman", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }
        }
    }
}