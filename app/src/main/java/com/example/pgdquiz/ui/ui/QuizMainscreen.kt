package com.example.pgdquiz.ui.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.QuizMode
import com.example.pgdquiz.ui.QuizType
import com.example.pgdquiz.ui.QuizModeSelection
import com.example.pgdquiz.ui.QuizTypeSelection
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun QuizApp(
    viewModel: QuizViewModel,
    onQuizTypeSelected: (QuizType) -> Unit
) {
    var selectedQuizType by remember { mutableStateOf<QuizType?>(null) }
    var selectedMode by remember { mutableStateOf<QuizMode?>(null) }
    val context = LocalContext.current

    when {
        selectedQuizType == null -> {
            QuizTypeSelection(
                tradeTom = painterResource(R.drawable.neonsign2),
                onSelectQuizType = { quizType ->
                    selectedQuizType = quizType
                },
                onQuizTypeSelected = onQuizTypeSelected
            )
        }

        selectedMode == null -> {
            QuizModeSelection(
                onSelectMode = { mode ->
                    selectedMode = mode
                    viewModel.loadQuestions(context, mode, selectedQuizType!!)
                }
            )
        }

        else -> {
            DrainLayout(
                viewModel = viewModel,
                onExit = { selectedMode = null },
                quizMode = selectedMode!!,
                quizType = selectedQuizType!!,
                onBackToModeSelect = { selectedMode = null }
            )
        }
    }
}
