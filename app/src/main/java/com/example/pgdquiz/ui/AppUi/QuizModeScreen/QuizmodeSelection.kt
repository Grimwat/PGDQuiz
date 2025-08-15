package com.example.pgdquiz.ui.AppUi.QuizModeScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.pgdquiz.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.AppUi.QuizModeScreen.Landscape.ModeButtonLandscape
import com.example.pgdquiz.ui.AppUi.QuizModeScreen.Portrait.ModeButtonPortrait
import com.example.pgdquiz.ui.Banner
import com.example.pgdquiz.ui.Data.QuizMode

import com.example.pgdquiz.ui.Data.QuizType

@Composable
fun QuizModeSelection(
    quizType: QuizType,
    modifier: Modifier = Modifier,
    onBackToQuizType: () -> Unit,
    lives: Int,
    streak: Int,
    onSelectMode: (mode: QuizMode) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


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

        if (isLandscape) {
            Row(modifier = modifier) {
                ModeButtonLandscape(
                    label = "Easy",
                    questionCountText = "(25 Questions)",
                    onClick = { onSelectMode(QuizMode.EASY) },
                    quizType = TODO(),
                    modifier = TODO(),
                )
                ModeButtonLandscape(
                    label = "Medium",
                    questionCountText = "(50 Questions)",
                    onClick = { onSelectMode(QuizMode.MEDIUM) },
                    quizType = quiztype,
                    modifier = TODO(),
                )
                ModeButtonLandscape(
                    label = "Hard",
                    questionCountText = "(100 Questions)",
                    onClick = { onSelectMode(QuizMode.HARD) },
                    quizType = TODO(),
                    modifier = TODO()
                )
            }
        } else {
            Spacer(modifier = Modifier.padding(16.dp))
            ModeButtonPortrait(
                label = "Easy",
                questionCountText = "(25 Questions)",
                logoRes = PGDLogo,
                onClick = { onSelectMode(QuizMode.EASY) }
            )
            ModeButtonPortrait(
                label = "Medium",
                questionCountText = "(50 Questions)",
                logoRes = PGDLogo,
                onClick = { onSelectMode(QuizMode.MEDIUM) }
            )
            ModeButtonPortrait(
                label = "Hard",
                questionCountText = "(100 Questions)",
                logoRes = PGDLogo,
                onClick = { onSelectMode(QuizMode.HARD) }
            )
        }
    }
}