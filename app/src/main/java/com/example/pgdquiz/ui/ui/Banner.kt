package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun Banner(
    examType: String,
    examEmoji: Painter,
    emojiCont: String,
    attempts: Int,
    icon: Painter,
    streak: String,
    streakCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.tertiary)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center

    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 120.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = examType,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier,
                textAlign = TextAlign.Center
            )
            Row {
                Image(
                    painter = icon,
                    contentDescription = streak,
                    modifier = Modifier
                        .size(40.dp),
                )
                Text(
                    text = streakCount.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.padding()) {
                Image(
                    painter = examEmoji,
                    contentDescription = emojiCont,
                    modifier = Modifier
                        .size(40.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = attempts.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}


@Preview
@Composable
fun Drainpreview() {
    PgdQuizTheme {
    }
}