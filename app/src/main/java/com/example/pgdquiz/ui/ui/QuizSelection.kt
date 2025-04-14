package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.pgdquiz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizTypeSelection(
    onSelectQuizType: (QuizType) -> Unit,
    tradeTom: Painter
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp)
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = tradeTom,
                contentDescription = "TradesmanTom",)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {

                Button(onClick = { onSelectQuizType(QuizType.DRAINLAYING) }) {
                    Text("Drainlaying")
                }

                Button(onClick = { onSelectQuizType(QuizType.PLUMBING) }) {
                    Text("Plumbing")
                }


                Button(onClick = { onSelectQuizType(QuizType.GASFITTING) }) {
                    Text("Gasfitting")
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun QuizTypeSelectionPreview() {
    QuizTypeSelection(
        tradeTom = painterResource(id = R.drawable.neonsign),
        onSelectQuizType = { }
    )
}