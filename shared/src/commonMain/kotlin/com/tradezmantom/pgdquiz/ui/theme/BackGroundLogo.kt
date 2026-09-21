package com.tradezmantom.pgdquiz.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import com.tradezmantom.pgdquiz.ui.data.QuizType
import org.jetbrains.compose.resources.painterResource
import pgdquiz.shared.generated.resources.Res
import pgdquiz.shared.generated.resources.arrow
import pgdquiz.shared.generated.resources.drainlogo
import pgdquiz.shared.generated.resources.gasslogo
import pgdquiz.shared.generated.resources.plumblogo

@Composable
fun BackgroundLogo (
    quizType: QuizType,
    modifier: Modifier = Modifier,
) {

    val PGDLogo = when (quizType) {
        QuizType.DRAIN_LAYING -> Res.drawable.drainlogo
        QuizType.PLUMBING -> Res.drawable.plumblogo
        QuizType.GASFITTING -> Res.drawable.gasslogo
        QuizType.DEFAULT -> Res.drawable.arrow
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
