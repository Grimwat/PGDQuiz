package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun Questionfield(
    modifier: Modifier = Modifier,
    question: String,
) {Box(
    modifier = modifier
        .fillMaxWidth(),
    contentAlignment = Alignment.Center)
{
        Image(
            painter = painterResource(R.drawable.questbox),
            contentDescription = "Questions",
        )
        Text(
            text = question,
            maxLines = 2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun Previewquestion() {
    PgdQuizTheme {
        Questionfield(
            question = "What minimum diameter\n is required for a vent stack",
        )
    }
}