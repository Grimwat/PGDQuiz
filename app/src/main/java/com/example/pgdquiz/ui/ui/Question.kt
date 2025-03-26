package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun QuestionField(
    modifier: Modifier = Modifier,
    question: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.outline),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            text = question,
            maxLines = 2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun PreviewQuestionField() {
    PgdQuizTheme {
        QuestionField(
            question = "What minimum diameter\n is required for a vent stack",
        )
    }
}