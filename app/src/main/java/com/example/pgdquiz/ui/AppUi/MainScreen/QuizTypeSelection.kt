package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.AppUi.MainScreen.QuizTypeButton
import com.example.pgdquiz.ui.Data.QuizType


@Composable
fun QuizTypeSelection(
    onSelectQuizType: (QuizType) -> Unit,
    tradeTom: Painter,
    onQuizTypeSelected: (QuizType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = tradeTom,
                contentDescription = "TradesmanTom",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(300.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuizTypeButton(
                text = "PLUMBING",
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f),
                logo = painterResource(R.drawable.plumblogo)
            ) { onQuizTypeSelected(QuizType.PLUMBING)
                onSelectQuizType(QuizType.PLUMBING) }

            QuizTypeButton(
                text = "GASFITTING",
                backgroundColor = MaterialTheme.colorScheme.onBackground,
                borderColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f),
                logo = painterResource(R.drawable.gasslogo)
            ) { onQuizTypeSelected(QuizType.GASFITTING)
                onSelectQuizType(QuizType.GASFITTING) }

            QuizTypeButton(
                text = "DRAINLAYING",
                backgroundColor = MaterialTheme.colorScheme.outline,
                borderColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.weight(1f),
                logo = painterResource(R.drawable.drainlogo)
            ) {onQuizTypeSelected(QuizType.DRAINLAYING)
                onSelectQuizType(QuizType.DRAINLAYING) }

        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

