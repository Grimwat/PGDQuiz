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
) {
    Box(modifier = modifier) {
        Row() {
            Text(
                text = question,
                modifier = Modifier
                    .weight(1f))
        }
    }
}

@Preview
@Composable
fun Previewquestion() {
    PgdQuizTheme {
    }
}