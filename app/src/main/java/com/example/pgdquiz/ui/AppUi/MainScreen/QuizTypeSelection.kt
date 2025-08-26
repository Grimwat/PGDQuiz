package com.example.pgdquiz.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.AppUi.MainScreen.QuizTypeButton
import com.example.pgdquiz.ui.Data.QuizType


@Composable
fun QuizTypeSelection(
    onSelectQuizType: (QuizType) -> Unit,
    tradeTom: Painter,
    onQuizTypeSelected: (QuizType) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = tradeTom,
                        contentDescription = "TradesmanTom",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(vertical = 16.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuizType.values()
                        .filter { it != QuizType.DEFAULT }
                        .forEach { type ->
                            QuizTypeButton(
                                text = type.name,
                                backgroundColor = when (type) {
                                    QuizType.PLUMBING -> MaterialTheme.colorScheme.onSurface
                                    QuizType.GASFITTING -> MaterialTheme.colorScheme.onBackground
                                    QuizType.DRAINLAYING -> MaterialTheme.colorScheme.outline
                                    else -> MaterialTheme.colorScheme.primary
                                },
                                borderColor = when (type) {
                                    QuizType.PLUMBING -> MaterialTheme.colorScheme.secondary
                                    QuizType.GASFITTING -> MaterialTheme.colorScheme.onPrimary
                                    QuizType.DRAINLAYING -> MaterialTheme.colorScheme.background
                                    else -> MaterialTheme.colorScheme.onSecondary
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F),
                                quizType = type
                            ) {
                                onQuizTypeSelected(type)
                                onSelectQuizType(type)
                            }
                        }
                }
                Spacer(modifier = Modifier.padding(end = 25.dp))
            }
        } else {
            Column {
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
                    QuizType.values()
                        .filter { it != QuizType.DEFAULT }
                        .forEach { type ->
                            QuizTypeButton(
                                text = type.name,
                                backgroundColor = when (type) {
                                    QuizType.PLUMBING -> MaterialTheme.colorScheme.onSurface
                                    QuizType.GASFITTING -> MaterialTheme.colorScheme.onBackground
                                    QuizType.DRAINLAYING -> MaterialTheme.colorScheme.outline
                                    else -> MaterialTheme.colorScheme.primary
                                },
                                borderColor = when (type) {
                                    QuizType.PLUMBING -> MaterialTheme.colorScheme.secondary
                                    QuizType.GASFITTING -> MaterialTheme.colorScheme.onPrimary
                                    QuizType.DRAINLAYING -> MaterialTheme.colorScheme.background
                                    else -> MaterialTheme.colorScheme.onSecondary

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                quizType = type
                            ) {
                                onQuizTypeSelected(type)
                                onSelectQuizType(type)
                            }
                        }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}



