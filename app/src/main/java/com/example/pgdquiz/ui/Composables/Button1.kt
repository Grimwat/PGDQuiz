package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun AnswerButton(
    modifier: Modifier = Modifier,
    drawableResourceId: Painter,
    questionResourceId: String,
    contentDescriptionId: String,
    onImageClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onImageClick,
            shape = RectangleShape,
            modifier = Modifier
                .background(Color.Transparent)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = drawableResourceId,
                    contentDescription = contentDescriptionId,
                    modifier = modifier
                )
                Text(
                    text = questionResourceId
                )
            }
        }
    }
}

@Composable
fun ButtonGrid(
    modifier: Modifier = Modifier
) {
    val buttonOff = painterResource(R.drawable.buttonoff)
    var questionset = "gg"
    val contentDes = "No Help here Cuz"
    Column{
        Row (modifier = modifier){
            AnswerButton(
                drawableResourceId = buttonOff,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            ) {}
            AnswerButton(
                drawableResourceId = buttonOff,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            ) {}
        }
        Row(modifier = Modifier) {
            AnswerButton(
                drawableResourceId = buttonOff,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            ) {}
            AnswerButton(
                drawableResourceId = buttonOff,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            ) {}
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