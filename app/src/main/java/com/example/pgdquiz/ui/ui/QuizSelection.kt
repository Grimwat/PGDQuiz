package com.example.pgdquiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizTypeSelection(
    onSelectQuizType: (QuizType) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choose Your Exam", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onSelectQuizType(QuizType.DRAINLAYING) }) {
            Text("Drainlaying")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onSelectQuizType(QuizType.PLUMBING) }) {
            Text("Plumbing")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onSelectQuizType(QuizType.GASFITTING) }) {
            Text("Gasfitting")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onBack() }) {
            Text("Back")
        }
    }
}