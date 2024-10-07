package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun Questionfield(
    modifier: Modifier = Modifier,
    question: String,
    icon: Painter,
    streakcount: String,
    questionbackground: Painter,
    streak: String
) {
    Box(modifier = modifier) {
        Image(
            painter = questionbackground,
            contentDescription = null
        )
        Row {
            Text(
                text = question)
            Row {
                Image(
                    painter = icon,
                    contentDescription = streak
                )
                Text(
                    text = streakcount)
            }
        }
    }
}