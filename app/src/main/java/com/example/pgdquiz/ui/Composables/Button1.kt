package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.PGDViewmodel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun AnswerButton(
    modifier: Modifier = Modifier,
    initialDrawableResourceId: Painter,
    selectedDrawableResourceId: Painter,
    questionResourceId: String,
    contentDescriptionId: String,
) {
    var isSelected by remember { mutableStateOf(false) }
    val drawableResourceId = if (isSelected) selectedDrawableResourceId else initialDrawableResourceId
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick =  {isSelected = !isSelected},
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified      
            ),
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
    val buttonOn = painterResource(R.drawable.buttonon)
    var questionset = "gg"
    val contentDes = "No Help here Cuz"
    Column{
        Row (modifier = modifier){
            AnswerButton(
                questionResourceId = questionset,
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f),
            )
            AnswerButton(
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier) {
            AnswerButton(
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            )
            AnswerButton(
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview
@Composable
fun GreetingPreview() {
    PgdQuizTheme {


    }
}