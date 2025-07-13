package com.example.pgdquiz.ui.AppUi


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.Data.QuizMode
import com.example.pgdquiz.ui.QuizType
import com.example.pgdquiz.ui.QuizModeSelection
import com.example.pgdquiz.ui.QuizTypeSelection
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun QuizApp(
    viewModel: QuizViewModel,
    onQuizTypeSelected: (QuizType) -> Unit,
) {
    var selectedQuizType by remember { mutableStateOf<QuizType?>(null) }
    var selectedMode by remember { mutableStateOf<QuizMode?>(null) }
    val context = LocalContext.current

    PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
        when {
            selectedQuizType == null -> {
                QuizTypeSelection(
                    onSelectQuizType = { quizType ->
                        selectedQuizType = quizType
                        onQuizTypeSelected(quizType)
                        viewModel.reset(quizType)
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
                    tradeTom = painterResource(
                        id = when (selectedQuizType ?: QuizType.DEFAULT) {
                            QuizType.DRAINLAYING -> R.drawable.happypoo2
                            QuizType.PLUMBING -> R.drawable.droplet
                            QuizType.GASFITTING -> R.drawable.pressure
                            QuizType.DEFAULT -> R.drawable.neonsign2
                        }
                    ),
                    onBackToQuizType = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    lives = viewModel.lives.value,
                    streak = viewModel.streakCount.value
                )
            }

            else -> {
                DrainLayout(
                    viewModel = viewModel,
                    onExit = {
                        selectedMode = null
                        selectedQuizType = null
                        viewModel.reset(QuizType.DEFAULT)
                    },
                    quizMode = selectedMode!!,
                    quizType = selectedQuizType!!,
                    examEmoji = painterResource(
                        id = when (selectedQuizType ?: QuizType.DEFAULT) {
                            QuizType.DRAINLAYING -> R.drawable.happypoo2
                            QuizType.PLUMBING -> R.drawable.droplet
                            QuizType.GASFITTING -> R.drawable.pressure
                            QuizType.DEFAULT -> R.drawable.neonsign2
                        }
                    ),
                    title = when (selectedQuizType ?: QuizType.DEFAULT) {
                        QuizType.DRAINLAYING -> "HappyPoo"
                        QuizType.PLUMBING -> "Droplet"
                        QuizType.GASFITTING -> "Pressure"
                        QuizType.DEFAULT -> "Default"
                    },
                    examCont = selectedQuizType!!.name,
                    onBackToModeSelect = {
                        selectedMode = null
                        viewModel.reset(selectedQuizType!!)
                    }
                )
            }
        }
    }
}