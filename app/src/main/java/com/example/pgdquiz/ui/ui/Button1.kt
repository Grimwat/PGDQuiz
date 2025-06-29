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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AnswerButton(
    optionText: String,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    isCorrect: Boolean,
    showCorrectAnswer: Boolean
) {
    val backgroundBrush = when {
        showCorrectAnswer && isCorrect -> Brush.radialGradient(
            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.outline),
            radius = 600f
        )

        isSelected -> Brush.radialGradient(
            colors = listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.primary),
            radius = 600f
        )

        else -> Brush.radialGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.background
            ),
            radius = 600f
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onButtonSelected,
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush = backgroundBrush)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionText,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
    @Composable
    fun ButtonGrid(
        modifier: Modifier = Modifier,
        viewModel: QuizViewModel = viewModel()
    ) {
        val currentQuestion = viewModel.currentQuestion.value ?: return
        val selectedAnswers = viewModel.selectedAnswers.value
        var showCorrectAnswer by remember { mutableStateOf(false) }

        Column(modifier = modifier) {
            currentQuestion.shuffledOptions?.forEach { option ->
                val isSelected = selectedAnswers.contains(option)
                val isCorrect = currentQuestion.isOptionCorrect(option)

                AnswerButton(
                    optionText = option,
                    isSelected = isSelected,
                    onButtonSelected = { viewModel.selectAnswer(option) },
                    modifier = Modifier.padding(vertical = 4.dp),
                    isCorrect = isCorrect,
                    showCorrectAnswer = showCorrectAnswer
                )
            }

            NextButton(
                onClick = {
                    showCorrectAnswer = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        showCorrectAnswer = false
                        viewModel.nextQuestion()
                    }, 1500)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
