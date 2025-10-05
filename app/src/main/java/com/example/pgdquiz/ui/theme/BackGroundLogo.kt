package com.example.pgdquiz.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.data.QuizType

@Composable
fun BackgroundLogo (
    quizType: QuizType,
    modifier: Modifier = Modifier,
) {

    val PGDLogo = when (quizType) {
        QuizType.DRAIN_LAYING -> R.drawable.drainlogo
        QuizType.PLUMBING -> R.drawable.plumblogo
        QuizType.GASFITTING -> R.drawable.gasslogo
        QuizType.DEFAULT -> R.drawable.arrow
    }

    Image(
        painter = painterResource(PGDLogo),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.2f),
        contentScale = ContentScale.None
    )
}