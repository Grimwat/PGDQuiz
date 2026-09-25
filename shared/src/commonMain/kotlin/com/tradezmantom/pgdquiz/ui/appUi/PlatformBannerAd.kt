package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tradezmantom.pgdquiz.ui.data.QuizType

/** Identifies which banner placement is being shown, so each gets its own ad unit ID
 * (and its own impression tracking in AdMob) without the platform-specific ID strings
 * leaking into commonMain. */
enum class BannerAdPlacement {
    QUIZ_SCREEN,
    QUIZ_SCREEN_DRAINLAYING,
    QUIZ_SCREEN_PLUMBING,
    QUIZ_SCREEN_GASFITTING,
    LIVES_LOST,
    QUIZ_COMPLETED,
    WELCOME;

    companion object
}

/** The in-quiz banner for the chosen trade, so each trade gets its own AdMob tracking. */
fun BannerAdPlacement.Companion.forQuizType(quizType: QuizType): BannerAdPlacement = when (quizType) {
    QuizType.DRAIN_LAYING -> BannerAdPlacement.QUIZ_SCREEN_DRAINLAYING
    QuizType.PLUMBING -> BannerAdPlacement.QUIZ_SCREEN_PLUMBING
    QuizType.GASFITTING -> BannerAdPlacement.QUIZ_SCREEN_GASFITTING
    QuizType.DEFAULT -> BannerAdPlacement.QUIZ_SCREEN
}

/** Renders a platform banner ad, or nothing where no ad SDK is wired up yet (iOS, for now). */
@Composable
expect fun PlatformBannerAd(placement: BannerAdPlacement, modifier: Modifier = Modifier)
