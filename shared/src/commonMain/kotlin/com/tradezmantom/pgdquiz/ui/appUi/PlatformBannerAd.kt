package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Identifies which banner placement is being shown, so each gets its own ad unit ID
 * (and its own impression tracking in AdMob) without the platform-specific ID strings
 * leaking into commonMain. */
enum class BannerAdPlacement {
    QUIZ_SCREEN,
    LIVES_LOST,
    QUIZ_COMPLETED,
    WELCOME,
}

/** Renders a platform banner ad, or nothing where no ad SDK is wired up yet (iOS, for now). */
@Composable
expect fun PlatformBannerAd(placement: BannerAdPlacement, modifier: Modifier = Modifier)
