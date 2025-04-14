package com.example.pgdquiz.ui.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
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
    showCorrectAnswer: Boolean,
    imageOptions: Boolean
) {
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
                if (imageOptions) {
                    val context = LocalContext.current
                    val imageResId = remember(optionText) {
                        context.resources.getIdentifier(optionText, "raw", context.packageName)
                    }

                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = optionText,
                            modifier = Modifier.size(64.dp)
                        )
                    } else {
                        Text("Image not found", color = Color.White)
                    }
                } else {
                    Text(text = optionText, color = Color.White)
                }
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
    val selectedAnswers = viewModel.selectedAnswers.value
    var showCorrectAnswer by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        currentQuestion.shuffledOptions.forEach { option ->
            val isSelected = selectedAnswers.contains(option)
            val isCorrect = currentQuestion.isOptionCorrect(option)

            AnswerButton(
                optionText = option,
                isSelected = isSelected,
                onButtonSelected = { viewModel.selectAnswer(option) },
                modifier = Modifier.padding(vertical = 4.dp),
                isCorrect = isCorrect,
                showCorrectAnswer = showCorrectAnswer,
                imageOptions = currentQuestion.imageOptions
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
