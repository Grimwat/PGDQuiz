package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.StateFlow

/**
 * Ad/billing entry points the quiz screens trigger in response to user actions. Screens read
 * this via [LocalQuizPlatformActions] instead of reaching for a platform Activity directly,
 * so the same screens can run on Android and iOS.
 */
interface QuizPlatformActions {
    val rewardedAdReady: StateFlow<Boolean>
    fun launchApprenticeSubscription()
    fun launchTradesmanPurchase()
    fun showInterstitialAd(onAdDismissed: () -> Unit)
    fun showRewardedAd(onRewardEarned: () -> Unit, onAdUnavailable: () -> Unit = {})
    fun showQuizTypeChangeInterstitial(onAdDismissed: () -> Unit)
}

val LocalQuizPlatformActions = staticCompositionLocalOf<QuizPlatformActions> {
    error("No QuizPlatformActions provided — wrap the quiz UI in CompositionLocalProvider(LocalQuizPlatformActions provides ...)")
}
