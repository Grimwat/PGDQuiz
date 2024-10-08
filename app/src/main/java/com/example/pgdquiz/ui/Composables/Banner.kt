package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun Banner(
    examType: String,
    examEmoji: Painter,
    emojiCont: String,
    attempts: String,
    icon: Painter,
    streak: String,
    streakcount: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = examType,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 150.dp, top = 5.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = icon,
            contentDescription = streak,
            modifier = Modifier
                .padding(start = 15.dp, bottom = 20.dp, top = 5.dp)
                .size(40.dp),
        )
        Text(
            text = streakcount,
            modifier = Modifier
                .padding(end = 20.dp, bottom = 20.dp, top = 5.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = examEmoji,
            contentDescription = emojiCont,
            modifier = Modifier
                .padding(start = 5.dp, bottom = 20.dp, top = 5.dp)
                .size(40.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = attempts,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 20.dp, bottom = 20.dp, top = 5.dp),
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun Drainpreview (){
    PgdQuizTheme {
        DrainLayout()
    }
}