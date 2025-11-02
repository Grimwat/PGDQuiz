package com.example.pgdquiz.ui.appUi.examLayoutScreen.landscape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.AnswerButton
import com.example.pgdquiz.ui.logic.QuizViewModel

@Composable
fun LandscapeGrid(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()
) {

    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    val currentQuestion = state.currentQuestion ?: return
    val selectedAnswers = state.selectedAnswer
    val showCorrectAnswer = state.showCorrectAnswer
    val options = currentQuestion.shuffledOptions ?: return
    val half = (options.size + 1) / 2

    Column(modifier = modifier)
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.take(half).forEach { option ->
                val isSelected = selectedAnswers.contains(option)
                val isCorrect = currentQuestion.isOptionCorrect(option)

                AnswerButton(
                    optionText = option,
                    isSelected = isSelected,
                    onButtonSelected = { viewModel.selectAnswer(option) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    isCorrect = isCorrect,
                    showCorrectAnswer = showCorrectAnswer,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.drop(half).forEach { option ->
                val isSelected = selectedAnswers.contains(option)
                val isCorrect = currentQuestion.isOptionCorrect(option)

                AnswerButton(
                    optionText = option,
                    isSelected = isSelected,
                    onButtonSelected = { viewModel.selectAnswer(option) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    isCorrect = isCorrect,
                    showCorrectAnswer = showCorrectAnswer,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

    }
}