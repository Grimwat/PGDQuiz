package com.example.pgdquiz.ui.appUi.examLayoutScreen

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.ui.AppUi.examLayoutScreen.NextButton
import com.example.pgdquiz.ui.AppUi.examLayoutScreen.QuestionField
import com.example.pgdquiz.ui.appUi.examLayoutScreen.landscape.LandscapeGrid
import com.example.pgdquiz.ui.appUi.examLayoutScreen.overlays.CongratulationsScreen
import com.example.pgdquiz.ui.appUi.examLayoutScreen.overlays.LivesLost
import com.example.pgdquiz.ui.appUi.examLayoutScreen.portrait.ButtonsPortrait
import com.example.pgdquiz.ui.data.QuizDifficulty
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.example.pgdquiz.ui.logic.QuizViewModel


@Composable
fun ExamLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel(),
    onExit: () -> Unit,
    examEmoji: Painter,
    title: String,
    examCont: String,
    quizDifficulty: QuizDifficulty,
    quizType: QuizType,
    onBackToModeSelect: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val state by viewModel.quizUiState.collectAsStateWithLifecycle()

    LaunchedEffect(quizType, quizDifficulty) {
        viewModel.startQuiz(quizDifficulty, quizType)
    }
Box(modifier = Modifier
    .fillMaxSize()
    .background(MaterialTheme.colorScheme.background)
    .padding(WindowInsets.navigationBars.asPaddingValues()),
    contentAlignment = Alignment.Center
){
    if (state.isLoading) {
        CircularProgressIndicator()
    }
     else if(state.currentQuestion != null) {

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
                    attempts = state.getCurrentQuizTypeLives(),
                    streakCount = state.answerStreak,
                    modifier = Modifier.fillMaxWidth(),
                    onBack = { onBackToModeSelect() }
                )

                Spacer(modifier = Modifier.padding(4.dp))

                if (isLandscape) {
                    Row() {
                        Box(
                            modifier = Modifier
                                .weight(0.85f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                QuestionField(
                                    question = state.currentQuestion,
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
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        QuestionField(
                            question = state.currentQuestion,
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

            if (state.getCurrentQuizTypeLives() <= 0) {
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

            if (state.showCongratulationsScreen) {
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
                                viewModel.startQuiz(quizDifficulty, quizType)
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
