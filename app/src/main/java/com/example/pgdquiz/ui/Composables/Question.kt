package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                maxLines = 2,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = modifier

                    )
        }
    }
}

@Preview
@Composable
fun Previewquestion() {
    PgdQuizTheme {
    }
}