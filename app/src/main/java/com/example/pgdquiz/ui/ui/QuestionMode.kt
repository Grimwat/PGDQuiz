package com.example.pgdquiz.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuizModeSelection(
    modifier: Modifier = Modifier,
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
            examType = "Drainlaying",
            examEmoji = painterResource(com.example.pgdquiz.R.drawable.happypoo2),
            emojiCont = "happyPoo",
            attempts = 0,
            icon = painterResource(com.example.pgdquiz.R.drawable.flame1),
            streak = "StreakCount",
            streakCount = 0,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(32.dp))
        Button(
            onClick = { onSelectMode(QuizMode.EASY) },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                disabledContentColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text("25 Questions")
        }

        Button(
            onClick = { onSelectMode(QuizMode.MEDIUM) },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                disabledContentColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text("50 Questions")
        }

        Button(
            onClick = { onSelectMode(QuizMode.HARD) },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                disabledContentColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text("100 Questions")
        }

        //BannerAd()
    }
}

@Preview
@Composable
fun QuizModePreview() {
    QuizModeSelection {  }
}
