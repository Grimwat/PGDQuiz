package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.pgdquiz.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.ui.Question
import com.example.pgdquiz.ui.QuizType

@Composable
fun QuestionField(
    quizType: QuizType,
    modifier: Modifier = Modifier,
    question: Question?,
) {
    val ExamId = when (quizType) {
        QuizType.GASFITTING -> R.drawable.gasslogo
        QuizType.PLUMBING -> R.drawable.plumblogo
        QuizType.DRAINLAYING -> R.drawable.drainlogo
        QuizType.DEFAULT -> null
    }
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background( MaterialTheme.colorScheme.primary)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.outline),
        contentAlignment = Alignment.Center
    ) {
        ExamId?.let { id ->
            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier
                    .alpha(0.2f),
            )
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = question?.question ?: "",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
@Preview
@Composable
fun PreviewQuestionField() {

}