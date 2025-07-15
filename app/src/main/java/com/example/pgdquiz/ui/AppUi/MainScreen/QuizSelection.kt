package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.pgdquiz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun QuizTypeButton(
    text: String,
    backgroundColor: Color,
    logo: Painter,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = 4.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.None
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                text.forEach { letter ->
                    Text(
                        text = letter.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


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

