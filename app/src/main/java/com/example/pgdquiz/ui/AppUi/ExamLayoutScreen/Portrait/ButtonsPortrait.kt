package com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.Portrait


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.Logic.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.AnswerButton
import com.example.pgdquiz.ui.AppUi.ExamLayoutScreen.NextButton
import com.example.pgdquiz.ui.Data.QuizType

@Composable
fun ButtonsPortrait(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    viewModel: QuizViewModel = viewModel()
) {
    val currentQuestion = viewModel.currentQuestion.value ?: return
    val selectedAnswers = viewModel.selectedAnswers.value
    val showCorrectAnswer by viewModel.showCorrectAnswer
    val context = LocalContext.current

    Column(modifier = modifier) {
        currentQuestion.shuffledOptions?.forEach { option ->
            val isSelected = selectedAnswers.contains(option)
            val isCorrect = currentQuestion.isOptionCorrect(option)

            AnswerButton(
                optionText = option,
                isSelected = isSelected,
                onButtonSelected = { viewModel.selectAnswer(option) },
                modifier = Modifier.padding(vertical = 2.dp),
                isCorrect = isCorrect,
                showCorrectAnswer = showCorrectAnswer,
                shape = RectangleShape
            )
        }

        NextButton(
            onClick = {
                viewModel.triggerShowCorrectAnswer(context)
            },
            quizType = quizType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}