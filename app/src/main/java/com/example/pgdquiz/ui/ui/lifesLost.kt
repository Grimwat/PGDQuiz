package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.example.pgdquiz.ui.theme.White


@Composable
fun LivesLost(
    modifier: Modifier = Modifier,
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onWatchAd,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.outline)
                        .border(
                            width = 4.dp,
                            MaterialTheme.colorScheme.primary
                        )
                )
                    {
                        Text(
                            text = "Gain 1 attempt",
                            color = Color.White
                        )
                    }
                Button(
                    onClick = onExit,
                    enabled = false,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.outline)
                        .border(
                            width = 4.dp,
                            MaterialTheme.colorScheme.primary
                        )
                )
                    {
                        Text(
                            text = "Leave Quiz",
                            color = Color.White
                        )

                }
            }
            Button(
                onClick = onUpgrade,
                enabled = false,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.outline)
                    .border(
                        width = 4.dp,
                        MaterialTheme.colorScheme.primary
                    )
            ) {
                    Text(
                        text = "Upgrade to Premium",
                        color = Color.White
                    )
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
