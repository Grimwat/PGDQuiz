package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun Questionfield(
    modifier: Modifier = Modifier,
    question: String,
    icon: Painter,
    streakcount: String,
    streak: String
) {
    Box(modifier = modifier) {
        Row() {
            Text(
                text = question,
                modifier = Modifier
                    .weight(1f))
            Row {
                Image(
                    painter = icon,
                    contentDescription = streak,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = streakcount,
                    modifier = Modifier
                        .weight(1f))
            }
        }
    }
}

@Preview
@Composable
fun Previewquestion() {
    PgdQuizTheme {
        Questionfield(
            question = "what Diamiter pipe is used for floor wastes",
            icon = painterResource(R.drawable.flame1),
            streakcount = "3",
            streak = "3")
    }
}