package com.example.pgdquiz.ui.appUi.mainScreen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.appUi.examLayoutScreen.ExamLayout
import com.example.pgdquiz.ui.appUi.quizModeScreen.QuizModeSelection
import com.example.pgdquiz.ui.data.QuizMode
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.example.pgdquiz.ui.logic.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun QuizApp(
    viewModel: QuizViewModel,
    onQuizTypeSelected: (QuizType) -> Unit,
) {

    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    var selectedQuizType by rememberSaveable { mutableStateOf<QuizType?>(null) }
    var selectedMode by rememberSaveable { mutableStateOf<QuizMode?>(null) }
    val context = LocalContext.current

    PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
        when {
            selectedQuizType == null -> {
                QuizTypeSelection(
                    onSelectQuizType = { quizType ->
                        selectedQuizType = quizType
                        onQuizTypeSelected(quizType)
                        //  viewModel.res(quizType)
                    },
                    onQuizTypeSelected = onQuizTypeSelected,

                    tradeTom = painterResource(id = R.drawable.neonsign3)
                )
            }

            selectedMode == null -> {
                QuizModeSelection(
                    quizType = selectedQuizType!!,
                    onSelectMode = { mode ->
                        selectedMode = mode
                        viewModel.startQuiz(context, mode, selectedQuizType!!)
                    },
                    onBackToQuizType = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    lives = state.getCurrentQuizTypeLives(),
                    streak = state.answerStreak
                )
            }

            else -> {
                ExamLayout(
                    viewModel = viewModel,
                    onExit = {
                        selectedMode = null
                        selectedQuizType = null
                        //   viewModel.reset(QuizType.DEFAULT)
                    },
                    quizMode = selectedMode!!,
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