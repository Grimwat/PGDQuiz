package com.example.pgdquiz.ui.AppUi.ExamLayoutScreen

//import com.example.pgdquiz.AppUi.Composables.BannerAd
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Landscape.LandscapeGrid
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Overlays.CongratulationsScreen
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Overlays.LivesLost
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Portrait.ButtonsPortrait
import com.example.pgdquiz.ui.Banner
import com.example.pgdquiz.ui.Data.QuizMode
import com.example.pgdquiz.ui.Data.QuizType
import com.example.pgdquiz.ui.Logic.QuizViewModel


@Composable
fun ExamLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel,
    onExit: () -> Unit,
    examEmoji: Painter,
    title: String,
    examCont: String,
    quizMode: QuizMode,
    quizType: QuizType,
    onBackToModeSelect: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    val lives = viewModel.currentLives
    val quizComplete = viewModel.quizComplete.value
    val question = viewModel.currentQuestion.value
    val selectedAnswers = viewModel.selectedAnswers.value


    LaunchedEffect(key1 = quizType to quizMode) {
        if (!viewModel.isQuizStarted) {
            viewModel.startQuiz(context, quizMode, quizType)
        }
    }
    if (question == null) {
        CircularProgressIndicator()
    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Banner(
                    quizType = quizType,
                    emojiCont = examCont,
                    attempts = lives,
                    streakCount = viewModel.streakCount.value,
                    modifier = Modifier.fillMaxWidth(),
                    onBack = { onBackToModeSelect() }
                )

                Spacer(modifier = Modifier.padding(4.dp))

                if (isLandscape) {
                    Row() {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.85f)
                        ) {
                            Column {
                                QuestionField(
                                    question = question,
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp),
                                    quizType = quizType
                                )
                                LandscapeGrid(
                                    viewModel = viewModel,
                                    modifier = modifier
                                )
                            }
                        }
                        NextButton(
                            onClick = { viewModel.triggerShowCorrectAnswer() },
                            quizType = quizType,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.15f)
                                .fillMaxWidth()
                        )
                    }
                }
                    else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        QuestionField(
                            question = question,
                            modifier = Modifier
                                .fillMaxWidth(),
                            quizType = quizType
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        ButtonsPortrait(
                            viewModel = viewModel,
                            quizType = quizType,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
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
                            onWatchAd = { viewModel.restoreLife() },
                            onExit = onExit,
                            examEmoji = examEmoji,
                            emojiCont = title,
                            quizType = quizType
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
                            examEmoji = examEmoji,
                            emojiCont = title,
                            onRestart = {
                                viewModel.startQuiz(context,quizMode, quizType)
                            },
                            onBackToModeSelect = onBackToModeSelect,
                            quizType = quizType
                        )
                    }
                }
            }
        }
    }
}
