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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier
) {
    var selectedButtonIndex by remember { mutableStateOf(-1) }

    val questionSet = listOf("optionOne", "optionTwo", "optionThree", "optionFour", "optionFive")
    val contentDes = "Quiz Option"

    Column(modifier = modifier) {
        for (i in questionSet.indices) {
            AnswerButton(
                buttonIndex = i,
                isSelected = selectedButtonIndex == i,
                onButtonSelected = { selectedButtonIndex = i },
                questionResourceId = questionSet[i],
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}


@Preview
@Composable
fun GreetingPreview() {
    PgdQuizTheme {
        ButtonGrid()

    }
}