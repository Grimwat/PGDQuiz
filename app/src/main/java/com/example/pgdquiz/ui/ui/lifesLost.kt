package com.example.pgdquiz.ui.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun LivesLost(
    modifier: Modifier = Modifier,
    examEmoji: Painter,
    emojiCont: String,
    onWatchAd: () -> Unit,
    onExit: () -> Unit,
    onUpgrade: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "All Out of Sh*ts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                painter = examEmoji,
                contentDescription = emojiCont

            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    onClick = onWatchAd,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = Color.White
                    )
                ) {
                    Text("Gain 1 attempt")
                }

                Button(
                    onClick = onExit,
                    enabled = false,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        disabledContentColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text("Leave Quiz")
                }
            }

            Button(
                onClick = onUpgrade,
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outline,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledContentColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Text("Upgrade to Premium")
            }
        }
    }
}




@Preview
@Composable
fun LivesPreview() {
    val viewModel = QuizViewModel()
    PgdQuizTheme {
    }
}
