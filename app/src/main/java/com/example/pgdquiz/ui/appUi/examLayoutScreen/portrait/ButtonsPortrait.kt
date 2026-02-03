package com.example.pgdquiz.ui.appUi.examLayoutScreen.portrait


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.ui.appUi.examLayoutScreen.AnswerButton
import com.example.pgdquiz.ui.appUi.examLayoutScreen.NextButton
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.logic.QuizViewModel

@Composable
fun ButtonsPortrait(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    viewModel: QuizViewModel = viewModel()
) {

    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    val currentQuestion = state.currentQuestion ?: return

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentQuestion == null) {
            return@LazyColumn
        }
        items(
            items = state.optionsAndAnswer,
            key = { option -> option }
        ) { option ->
            val isSelected = state.selectedAnswer == option
            val isCorrect = currentQuestion.isOptionCorrect(option)

            AnswerButton(
                optionText = option,
                isSelected = isSelected,
                onButtonSelected = { viewModel.selectAnswer(option) },
                modifier = Modifier,
                isCorrect = isCorrect,
                showCorrectAnswer = state.showCorrectAnswer,
                shape = RectangleShape
            )
        }
        item {
            NextButton(
                onClick = {
                    viewModel.triggerShowCorrectAnswer()
                },
                quizType = quizType,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
}
