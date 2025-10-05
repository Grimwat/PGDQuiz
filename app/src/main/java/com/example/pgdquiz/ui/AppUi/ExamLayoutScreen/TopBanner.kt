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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.pgdquiz.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.ui.data.QuizType
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

@Composable
fun Banner(
    quizType: QuizType,
    emojiCont: String,
    attempts: Int,
    streakCount: Int,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val (title, examEmoji) = when (quizType) {
        QuizType.DRAIN_LAYING -> "Drainlaying" to R.drawable.happypoo2
        QuizType.PLUMBING -> "Plumbing" to R.drawable.drop2
        QuizType.GASFITTING -> "Gasfitting" to R.drawable.pressure2
        QuizType.DEFAULT -> "Quiz App" to R.drawable.arrow
    }
    val PGDLogo = when (quizType) {
        QuizType.DRAIN_LAYING -> R.drawable.drainlogo
        QuizType.PLUMBING -> R.drawable.plumblogo
        QuizType.GASFITTING -> R.drawable.gasslogo
        QuizType.DEFAULT -> R.drawable.arrow
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val verticalPadding = if (isLandscape) 8.dp else 16.dp
    val iconSize = if (isLandscape) 24.dp else 32.dp
    val bannerEnd = if (isLandscape) 50.dp else 0.dp
    val bannerStart = if (isLandscape) 8.dp else 0.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.tertiary)
            .border(width = 4.dp, color = MaterialTheme.colorScheme.surface)
            .padding(end = bannerEnd),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack,
                modifier = Modifier.padding(start = bannerStart)
                   ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(iconSize)
                )
            }
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 28.dp, top = 16.dp),
                    textAlign = TextAlign.Center
                )


            Row (
                modifier = Modifier
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(R.drawable.flame1),
                    contentDescription = "Streak Icon",
                    modifier = Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = streakCount.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Image(
                    painter = painterResource(examEmoji),
                    contentDescription = emojiCont,
                    modifier = Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = attempts.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}



@Preview
@Composable
fun Banner(

) {

}
