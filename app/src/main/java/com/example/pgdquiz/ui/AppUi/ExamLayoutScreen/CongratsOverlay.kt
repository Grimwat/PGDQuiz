package com.example.pgdquiz.ui.AppUi.ExamLayoutScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.QuizType

@Composable
fun CongratulationsScreen(
    modifier: Modifier = Modifier,
    examEmoji: Painter,
    emojiCont: String,
    quizType: QuizType,
    onRestart: () -> Unit,
    onBackToModeSelect: () -> Unit
) {
    val (title,examEmoji) = when (quizType) {
        QuizType.DRAINLAYING -> "HappyPoo" to R.drawable.happypoo2
        QuizType.PLUMBING -> "Droplet" to R.drawable.drop2
        QuizType.GASFITTING -> "Pressure" to R.drawable.pressure2
        QuizType.DEFAULT -> "Nothing G" to  R.drawable.arrow
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Congratulations!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "You've completed all the questions!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            Image(
                painter = painterResource(id = examEmoji),
                contentDescription = title
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    onClick = onRestart,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = Color.White
                    )
                ) {
                    Text("Restart")
                }

                Button(
                    onClick = onBackToModeSelect,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = Color.White
                    )
                ) {
                    Text("Back")
                }
            }
        }
    }
}