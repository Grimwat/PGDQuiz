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
    buttonIndex: Int,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    initialDrawableResourceId: Painter,
    selectedDrawableResourceId: Painter,
    questionResourceId: String,
    contentDescriptionId: String,
) {
    val drawableResourceId = if (isSelected) selectedDrawableResourceId else initialDrawableResourceId
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick =  {onButtonSelected()},
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
    var selectedButtonIndex by remember { mutableStateOf(-1) }
    val buttonOff = painterResource(R.drawable.buttonoff)
    val buttonOn = painterResource(R.drawable.buttonon)
    var questionset = "gg"
    val contentDes = "No Help here Cuz"
    Column{
        Row (modifier = modifier){
            AnswerButton(
                buttonIndex = 0,
                isSelected = selectedButtonIndex == 0,
                onButtonSelected = { selectedButtonIndex = 0 },
                questionResourceId = questionset,
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f),
            )
            AnswerButton(
                buttonIndex = 1,
                isSelected = selectedButtonIndex == 1,
                onButtonSelected = { selectedButtonIndex = 1 },
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier) {
            AnswerButton(
                buttonIndex = 2,
                isSelected = selectedButtonIndex == 2,
                onButtonSelected = { selectedButtonIndex = 2 },
                initialDrawableResourceId = buttonOff,
                selectedDrawableResourceId = buttonOn,
                questionResourceId = questionset,
                contentDescriptionId = contentDes,
                modifier = Modifier.weight(1f)
            )
            AnswerButton(
                buttonIndex = 3,
                isSelected = selectedButtonIndex == 3,
                onButtonSelected = { selectedButtonIndex = 3 },
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