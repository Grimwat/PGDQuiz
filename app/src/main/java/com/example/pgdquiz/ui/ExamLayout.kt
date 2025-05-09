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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.ui.ButtonGrid
import com.example.pgdquiz.ui.ui.CongratulationsScreen
import com.example.pgdquiz.ui.ui.LivesLost
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
    val lives = viewModel.lives.value
    val quizComplete = viewModel.quizComplete.value
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
                attempts = viewModel.lives.value,
                streakCount = viewModel.streakCount.value,
                modifier = Modifier.fillMaxWidth(),
                onBack = {
                    onBackToModeSelect()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            QuestionField(
                question = viewModel.currentQuestion,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                quizType = quizType,
            )
            ButtonGrid(viewModel = viewModel)
//        BannerAd()
        }
        if (lives <= 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    LivesLost(
                        onWatchAd = {
                            viewModel.restoreLife()
                        },
                        onExit = {onExit()},
                        examEmoji = painterResource(R.drawable.happypoo2),
                        emojiCont = "happyPoo",
                    )
                }
            }
        }
        if (quizComplete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
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
}

