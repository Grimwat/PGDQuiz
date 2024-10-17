package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
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
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.banner__2_),
            contentDescription = "Banner"
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = examType,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 140.dp, top = 20.dp, bottom = 20.dp),
                textAlign = TextAlign.Center
            )
            Row {
                Image(
                    painter = icon,
                    contentDescription = streak,
                    modifier = Modifier
                        .padding(start = 30.dp, bottom = 20.dp, top = 15.dp)
                        .size(40.dp),
                )
                Text(
                    text = streakcount,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 20.dp, top = 20.dp),
                    textAlign = TextAlign.Center
                )
            }
            Row {
                Image(
                    painter = examEmoji,
                    contentDescription = emojiCont,
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 15.dp)
                        .size(40.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = attempts,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 25.dp, bottom = 20.dp, top = 20.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}


@Preview
@Composable
fun Drainpreview (){
    PgdQuizTheme {
        Banner(
            examType = "Drainlaying",
            examEmoji = painterResource(R.drawable.happypoo),
            emojiCont = "happyPoo",
            attempts = "3",
            icon = painterResource(R.drawable.flame1),
            streak = "Streak Count",
            streakcount = "3")
    }
}