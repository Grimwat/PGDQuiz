package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.pgdquiz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun QuizTypeSelection(
    onSelectQuizType: (QuizType) -> Unit,
    tradeTom: Painter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            painter = tradeTom,
            contentDescription = "TradesmanTom",
            modifier = Modifier
                .fillMaxWidth(),

            )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = { onSelectQuizType(QuizType.DRAINLAYING) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Unspecified
                ),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.outline)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.background
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Drainlaying",
                        color = Color.White
                    )
                }
            }

            Button(
                onClick = { onSelectQuizType(QuizType.PLUMBING) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Unspecified
                ),
                ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onSurface)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Plumbing",
                        color = Color.White
                    )
                }
            }


                Button(
                onClick = { onSelectQuizType(QuizType.GASFITTING) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Unspecified
                ),

                ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Gasfitting",
                        color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizTypeSelectionPreview() {
    PgdQuizTheme(quizType = QuizType.DEFAULT) {
        QuizTypeSelection(
            tradeTom = painterResource(id = R.drawable.neonsign),
            onSelectQuizType = {}
        )
    }
}