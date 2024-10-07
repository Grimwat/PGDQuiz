package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource


@Composable
fun AnswerButton(
    modifier: Modifier = Modifier,
    drawableResourceId: Int,
    questionResourceId: Int,
    contentDescriptionId: Int,
    onImageClick: () -> Unit
){
    Button(
        onClick = onImageClick) {
        Image(
            painter = painterResource(drawableResourceId),
            contentDescription = stringResource(contentDescriptionId))
        Text(
            text = stringResource(questionResourceId))
        
    }
}

@Composable
fun ButtonGrid(){
    Column {
        Row {
            AnswerButton(
                drawableResourceId =,
                questionResourceId =,
                contentDescriptionId =
            ) {}
            AnswerButton(
                drawableResourceId =,
                questionResourceId =,
                contentDescriptionId =
            ) {}
        }
        Row {
            AnswerButton(
                drawableResourceId = ,
                questionResourceId = ,
                contentDescriptionId = ) {}
            AnswerButton(
                drawableResourceId = ,
                questionResourceId = ,
                contentDescriptionId = ) {}
        }
    }
    }