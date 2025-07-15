package com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Overlays

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.QuizType

@Composable
fun LivesLost(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    examEmoji: Painter,
    emojiCont: String,
    onWatchAd: () -> Unit,
    onExit: () -> Unit,
    onUpgrade: () -> Unit = {}
) {
    val (title,examEmoji) = when (quizType) {
        QuizType.DRAINLAYING -> "HappyPoo" to R.drawable.happypoo2
        QuizType.PLUMBING -> "Droplet" to R.drawable.drop2
        QuizType.GASFITTING -> "Pressure" to R.drawable.pressure2
        QuizType.DEFAULT -> "Nothing G" to  R.drawable.arrow
    }
    val drainlayingMessages = listOf(
        "Got laid out",
        "Do you even lay?",
        "That's a Box",
        "Bungs in the wrong place cuz",
        "Well that'll fail inspection",
        "The Plumber Could Do It..",
        "Its Backfalling!",
        "Down the drain, huh?",
        "That one flushed your streak!",
        "Drain again next time!"
    )

    val plumbingMessages = listOf(
        "You sprung a leak!",
        "Thats a Box",
        "Pressure Dropped",
        "I wont tell the boss",
        "Gassfitters laughing bro",
        "Well that'll fail inspection",
        "You forgot a Crimp..",
        "No better then a Drainlayer..",
        "Pipe dream ended there.",
        "Try plunging into it again!"
    )

    val gasfittingMessages = listOf(
        "Gas leak detected... in your answers!",
        "Boom! That one was tricky.",
        "I wont tell the boss",
        "Thats a Box",
        "Atmospheric Pressure maybe?",
        "Pressure dropped..",
        "Should have been a Plumber..",
        "Get the leak check..",
        "You sure you tightend that fitting?",
        "Better ventilate your thinking!"
    )
    val messages = when (quizType) {
        QuizType.DRAINLAYING -> drainlayingMessages
        QuizType.PLUMBING -> plumbingMessages
        QuizType.GASFITTING -> gasfittingMessages
        else -> listOf("Oops, try again!")
    }


    val message = remember(quizType) { messages.random() }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Image(
                painter = painterResource(id = examEmoji),
                contentDescription = emojiCont

            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    onClick = onWatchAd,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = Color.White
                    )
                ) {
                    Text("Gain 1 attempt")
                }

                Button(
                    onClick = onExit,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        disabledContainerColor = MaterialTheme.colorScheme.outline,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Leave Quiz")
                }
            }

            Button(
                onClick = onUpgrade,
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outline,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledContentColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Text("Upgrade to Premium")
            }
        }
    }
}


@Preview
@Composable
fun LivesPreview() {
}
