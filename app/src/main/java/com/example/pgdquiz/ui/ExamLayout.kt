package com.example.pgdquiz.ui

//import com.example.pgdquiz.ui.Composables.BannerAd
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.ui.ButtonGrid
import com.example.pgdquiz.ui.ui.CongratulationsScreen
import com.example.pgdquiz.ui.ui.LivesLost
import com.example.pgdquiz.ui.ui.OverlayBox
import com.example.pgdquiz.ui.ui.QuestionField


@Composable
fun DrainLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel,
    onExit: () -> Unit,
    examEmoji: Painter,
    examCont: String,
    quizMode: QuizMode,
    quizType: QuizType,
    onBackToModeSelect: () -> Unit
) {
    val lives by remember { derivedStateOf { viewModel.lives.value } }
    val quizComplete by remember { derivedStateOf { viewModel.quizComplete.value } }
    val question = viewModel.currentQuestion
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Banner(
                quizType = quizType,
                emojiCont = examCont,
                attempts = lives,
                streakCount = viewModel.streakCount.value,
                modifier = Modifier.fillMaxWidth(),
                onBack = onBackToModeSelect
            )
            Spacer(modifier = Modifier.padding(4.dp))

            if (question != null) {
                QuestionField(
                    question = question,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    quizType = quizType,
                )
                ButtonGrid(viewModel = viewModel)
            } else {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Optional Ad:
            // BannerAd()
        }

        if (lives <= 0) {
            OverlayBox {
                LivesLost(
                    onWatchAd = { viewModel.restoreLife() },
                    onExit = onExit,
                    examEmoji = painterResource(R.drawable.happypoo2),
                    emojiCont = "happyPoo",
                )
            }
        }

        if (quizComplete) {
            OverlayBox {
                CongratulationsScreen(
                    examEmoji = painterResource(R.drawable.happypoo2),
                    emojiCont = "happyPoo",
                    onRestart = {
                        viewModel.restartQuiz(quizMode, context, quizType)
                    },
                    onBackToModeSelect = onBackToModeSelect
                )
            }
        }
    }
}