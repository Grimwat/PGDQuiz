package com.example.pgdquiz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.AppUi.QuizModeScreen.Portrait.ModeButton
import com.example.pgdquiz.ui.Data.QuizMode
import com.example.pgdquiz.ui.Data.QuizType

@Composable
fun QuizModeSelection(
    quizType: QuizType,
    tradeTom: Painter,
    modifier: Modifier = Modifier,
    onBackToQuizType: () -> Unit,
    lives: Int,
    streak: Int,
    onSelectMode: (mode: QuizMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Banner(
            quizType = quizType,
            emojiCont = "Exam Icon",
            attempts = lives,
            streakCount = streak,
            modifier = Modifier.fillMaxWidth(),
            onBack = onBackToQuizType
        )

        Spacer(modifier = Modifier.padding(16.dp))

        val PGDLogo = when (quizType) {
            QuizType.DRAINLAYING -> R.drawable.drainlogo
            QuizType.PLUMBING -> R.drawable.plumblogo
            QuizType.GASFITTING -> R.drawable.gasslogo
            QuizType.DEFAULT -> R.drawable.arrow
        }

        ModeButton(
            label = "Easy",
            questionCountText = "(25 Questions)",
            logoRes = PGDLogo,
            onClick = { onSelectMode(QuizMode.EASY) }
        )

        ModeButton(
            label = "Medium",
            questionCountText = "(50 Questions)",
            logoRes = PGDLogo,
            onClick = { onSelectMode(QuizMode.MEDIUM) }
        )

        ModeButton(
            label = "Hard",
            questionCountText = "(100 Questions)",
            logoRes = PGDLogo,
            onClick = { onSelectMode(QuizMode.HARD) }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun QuizModeSelectionPreview() {
    QuizModeSelection(
        quizType = QuizType.PLUMBING,
        tradeTom = painterResource(id = R.drawable.plumblogo),
        lives = 3,
        streak = 5,
        onBackToQuizType = {},
        onSelectMode = {}
    )
}