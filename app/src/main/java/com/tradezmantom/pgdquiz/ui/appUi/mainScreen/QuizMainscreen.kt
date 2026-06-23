package com.tradezmantom.pgdquiz.ui.appUi.mainScreen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tradezmantom.pgdquiz.R
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.ExamLayout
import com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen.QuizModeSelection
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.ui.data.hasNoAds
import com.tradezmantom.pgdquiz.ui.data.hasUnlimitedLives
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel
import com.tradezmantom.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun QuizMainScreen(
    viewModel: QuizViewModel,
    onQuizTypeSelected: (QuizType) -> Unit,
) {

    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    var selectedQuizType by rememberSaveable { mutableStateOf<QuizType?>(null) }
    var selectedMode by rememberSaveable { mutableStateOf<QuizDifficulty?>(null) }


    PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentAlignment = Alignment.Center

            )
            { CircularProgressIndicator() }
        }
        when {
            selectedQuizType == null -> {
                QuizTypeSelection(
                    onSelectQuizType = { quizType ->
                        selectedQuizType = quizType
                        onQuizTypeSelected(quizType)

                    },
                    onQuizTypeSelected = onQuizTypeSelected,

                    tradeTom = painterResource(id = R.drawable.neonsign3)
                )
            }

            selectedMode == null -> {
                BackHandler {
                    selectedQuizType = null
                }
                LaunchedEffect(Unit) {
                    viewModel.refreshForDifficultyScreen()
                }
                QuizModeSelection(
                    quizType = selectedQuizType!!,
                    onSelectDifficulty = { mode ->
                        selectedMode = mode
                        viewModel.startQuiz(mode, selectedQuizType!!)
                    },
                    onBackToQuizType = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    lives = state.getCurrentQuizTypeLives(),
                    streak = state.answerStreak,
                    showAds = !state.hasNoAds(),
                    hasUnlimitedLives = state.hasUnlimitedLives()
                )
            }

            else -> {
                BackHandler {
                    selectedMode = null
                }
                ExamLayout(
                    viewModel = viewModel,
                    onExit = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    quizDifficulty = selectedMode!!,
                    quizType = selectedQuizType!!,
                    examEmoji = painterResource(
                        id = when (selectedQuizType ?: QuizType.DEFAULT) {
                            QuizType.DRAIN_LAYING -> R.drawable.happypoo2
                            QuizType.PLUMBING -> R.drawable.droplet
                            QuizType.GASFITTING -> R.drawable.pressure
                            QuizType.DEFAULT -> R.drawable.neonsign2
                        }
                    ),
                    title = when (selectedQuizType ?: QuizType.DEFAULT) {
                        QuizType.DRAIN_LAYING -> "HappyPoo"
                        QuizType.PLUMBING -> "Droplet"
                        QuizType.GASFITTING -> "Pressure"
                        QuizType.DEFAULT -> "Default"
                    },
                    examCont = selectedQuizType!!.name,
                    onBackToModeSelect = {
                        selectedMode = null
                        //    viewModel.reset(selectedQuizType!!)
                    }
                )
            }
        }
    }
}