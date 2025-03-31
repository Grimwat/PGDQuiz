package com.example.pgdquiz.ui.ui

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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.Grey
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun AnswerButton(
    buttonIndex: Int,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    questionResourceId: String
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
                        if (isSelected) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.primary
                    )
                    .border(
                        width = 4.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = questionResourceId, color = Color.White)
            }
        }
    }
}

@Composable
fun ButtonGrid(
    viewModel: QuizViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentQuestion = viewModel.currentQuestion
    var selectedButtonIndex by remember { mutableStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }  // Changed from val to var

    Column(modifier = modifier) {
        Text(
            text = currentQuestion.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        currentQuestion.shuffledOptions.forEachIndexed { index, option ->
            AnswerButton(
                buttonIndex = index,
                isSelected = selectedButtonIndex == index,
                onButtonSelected = {
                    selectedButtonIndex = index
                    isAnswered = true  // Fixed incorrect comma
                },
                questionResourceId = option,
                initialContent = { Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) }, // Fixed undefined Grey
                selectedContent = {
                    Box(
                        modifier = Modifier.background(
                            if (index == currentQuestion.correctAnswerIndex)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                    )
                },
                contentDescriptionId = "Answer Option $index",
                modifier = Modifier.padding(4.dp)
            )
        }

        // Placed inside Column
        NextButton(
            onClick = {
                selectedButtonIndex = -1
                isAnswered = false
                viewModel.nextQuestion()
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
fun GreetingPreview() {
    PgdQuizTheme {
        ButtonGrid()

    }
}