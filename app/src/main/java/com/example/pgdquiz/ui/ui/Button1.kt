package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun AnswerButton(
    buttonIndex: Int,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    initialContent: @Composable () -> Unit,
    selectedContent: @Composable () -> Unit,
    questionResourceId: String,
    contentDescriptionId: String,
) {
    val drawableResourceId =
        if (isSelected) selectedContent else initialContent
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
            modifier = Modifier
                .background(Color.Transparent)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .border(width = 4.dp, color = MaterialTheme.colorScheme.outline)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    selectedContent()
                } else {
                    initialContent()
                }
                Text(text = questionResourceId)
            }
        }
    }
}


@Composable
fun ButtonGrid(
    modifier: Modifier = Modifier
) {
    var selectedButtonIndex by remember { mutableStateOf(-1) }
    var buttonOff: @Composable () -> Unit = {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .border(width = 4.dp, color = MaterialTheme.colorScheme.outline),
            contentAlignment = Alignment.Center
        ){}
    }
    var buttonOn: @Composable () -> Unit = {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.outline)
                .border(width = 4.dp, color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ){}
    }
    var questionset = "placeholder"
    val contentDes = "No Help here Cuz"
    Column(modifier = modifier) {

        AnswerButton(
            buttonIndex = 0,
            isSelected = selectedButtonIndex == 0,
            onButtonSelected = { selectedButtonIndex = 0 },
            questionResourceId = questionset,
            initialContent = buttonOff,
            selectedContent = buttonOn,
            contentDescriptionId = contentDes,
            modifier = Modifier.padding(0.1.dp)
        )
        AnswerButton(
            buttonIndex = 0,
            isSelected = selectedButtonIndex == 0,
            onButtonSelected = { selectedButtonIndex = 0 },
            questionResourceId = questionset,
            initialContent = buttonOff,
            selectedContent = buttonOn,
            contentDescriptionId = contentDes,
            modifier = Modifier.padding(0.1.dp)
        )

        AnswerButton(
            buttonIndex = 0,
            isSelected = selectedButtonIndex == 0,
            onButtonSelected = { selectedButtonIndex = 0 },
            questionResourceId = questionset,
            initialContent = buttonOff,
            selectedContent = buttonOn,
            contentDescriptionId = contentDes,
            modifier = Modifier.padding(0.1.dp)
        )
        AnswerButton(
            buttonIndex = 0,
            isSelected = selectedButtonIndex == 0,
            onButtonSelected = { selectedButtonIndex = 0 },
            questionResourceId = questionset,
            initialContent = buttonOff,
            selectedContent = buttonOn,
            contentDescriptionId = contentDes,
            modifier = Modifier.padding(0.1.dp)
        )
        AnswerButton(
            buttonIndex = 0,
            isSelected = selectedButtonIndex == 0,
            onButtonSelected = { selectedButtonIndex = 0 },
            questionResourceId = questionset,
            initialContent = buttonOff,
            selectedContent = buttonOn,
            contentDescriptionId = contentDes,
            modifier = Modifier.padding(0.1.dp)
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