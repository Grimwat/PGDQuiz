package com.example.pgdquiz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.R

@Composable
fun QuizModeSelection(
    quizType: QuizType,
    tradeTom: Painter,
    modifier: Modifier = Modifier,
    onBackToQuizType: () -> Unit,
    lives: Int,
    streak: Int,
    onSelectMode: (mode: QuizMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Banner(
            quizType = quizType,
            emojiCont = "Exam Icon",
            attempts = lives,
            streakCount = streak,
            modifier = Modifier.fillMaxWidth(),
            onBack = onBackToQuizType
        )

        Spacer(modifier = Modifier.padding(16.dp))

        val PGDLogo = when (quizType) {
            QuizType.DRAINLAYING -> R.drawable.drainlogo
            QuizType.PLUMBING -> R.drawable.plumblogo
            QuizType.GASFITTING -> R.drawable.gasslogo
            QuizType.DEFAULT -> R.drawable.arrow
        }

        Button(
            onClick = { onSelectMode(QuizMode.EASY) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 36.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = PGDLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.15f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Easy",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,)
                    Text("(25 Questions)",
                        color = Color.White,)
                }
            }
        }

        Button(
            onClick = { onSelectMode(QuizMode.MEDIUM) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 36.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = PGDLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.15f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Medium",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                    Text("(50 Questions)",
                        color = Color.White)
                }
            }
        }

        Button(
            onClick = { onSelectMode(QuizMode.HARD) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 36.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = PGDLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.15f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Hard",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,)
                    Text("(100 Questions)",
                        color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizModePreview() {
}
