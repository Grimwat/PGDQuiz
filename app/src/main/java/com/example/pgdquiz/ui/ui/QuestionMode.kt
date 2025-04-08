package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun QuizModeSelection(
    modifier: Modifier = Modifier,
    onSelectMode: (mode: QuizMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
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

        Button(onClick = { onSelectMode(QuizMode.EASY) }) {
            Text("25 Questions")
        }

        Button(onClick = { onSelectMode(QuizMode.MEDIUM) }) {
            Text("50 Questions")
        }

        Button(onClick = { onSelectMode(QuizMode.HARD) }) {
            Text("100 Questions")
        }

        //BannerAd()
    }
}

@Composable
fun Text(x0: String) {
    TODO("Not yet implemented")
}

@Composable
fun Button(onClick: () -> Unit, content: @Composable () -> Text) {
    TODO("Not yet implemented")
}