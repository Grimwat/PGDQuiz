package com.example.pgdquiz.ui.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.ui.answers


@Composable
fun AnswerButton(
    buttonIndex: Int,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    questionResourceId: String,
    isCorrect: Boolean,
    showCorrectAnswer: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onButtonSelected() },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified
            ),
            modifier = Modifier.background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            showCorrectAnswer && isCorrect -> MaterialTheme.colorScheme.secondary
                            isSelected -> MaterialTheme.colorScheme.outline
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                    .border(
                        width = 4.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = questionResourceId,
                    textAlign = Alignment.Center,
                    color = Color.White)
            }
        }
    }
}

@Composable
fun ButtonGrid(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()
) {
    val currentQuestion = viewModel.currentQuestion ?: return
    var selectedButtonIndex by remember { mutableStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }
    var showCorrectAnswer by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        currentQuestion?.answers()?.forEachIndexed { index, option ->
            AnswerButton(
                buttonIndex = index,
                isSelected = selectedButtonIndex == index,
                onButtonSelected = {
                    selectedButtonIndex = index
                    isAnswered = true
                    viewModel.selectAnswer(option)
                },
                questionResourceId = option,
                modifier = modifier,
                isCorrect = option == currentQuestion.answer,
                showCorrectAnswer = showCorrectAnswer
            )
        }

        NextButton(
            onClick = {
                showCorrectAnswer = true
                Handler(Looper.getMainLooper()).postDelayed({
                    selectedButtonIndex = -1
                    isAnswered = false
                    showCorrectAnswer = false
                    viewModel.nextQuestion()
                }, 1500)
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
fun GreetingPreview() {
    val viewModel = QuizViewModel()
    PgdQuizTheme {
        viewModel.loadQuestionsFromRawResource(LocalContext.current)

        ButtonGrid(viewModel = viewModel)

    }
}