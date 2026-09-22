package com.tradezmantom.pgdquiz

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.tradezmantom.pgdquiz.billing.BillingManager
import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.billing.PurchaseLauncher
import com.tradezmantom.pgdquiz.data.database.createAppDatabase
import com.tradezmantom.pgdquiz.data.repository.DefaultQuizDatastore
import com.tradezmantom.pgdquiz.domain.DefaultQuestionLoader
import com.tradezmantom.pgdquiz.ui.appUi.LocalQuizPlatformActions
import com.tradezmantom.pgdquiz.ui.appUi.QuizPlatformActions
import com.tradezmantom.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel
import com.tradezmantom.pgdquiz.ui.theme.PgdQuizTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.UIKit.UIViewController

// No ad SDK wired up on iOS yet — interstitial/rewarded just fall through immediately,
// and the banner slot (PlatformBannerAd) already renders nothing on iOS. Billing goes
// through the same shared BillingManager (StoreKit 1) used on Android.
private class IosQuizPlatformActions(
    private val billingManager: BillingManager
) : QuizPlatformActions {
    private val _rewardedAdReady = MutableStateFlow(false)
    override val rewardedAdReady: StateFlow<Boolean> = _rewardedAdReady

    override fun launchApprenticeSubscription() {
        billingManager.launchApprenticeFlow(PurchaseLauncher())
    }

    override fun launchTradesmanPurchase() {
        billingManager.launchTradesmanFlow(PurchaseLauncher())
    }

    override fun showInterstitialAd(onAdDismissed: () -> Unit) {
        onAdDismissed()
    }

    override fun showRewardedAd(onRewardEarned: () -> Unit, onAdUnavailable: () -> Unit) {
        onAdUnavailable()
    }

    override fun showQuizTypeChangeInterstitial(onAdDismissed: () -> Unit) {
        onAdDismissed()
    }
}

@Suppress("FunctionName")
fun MainViewController(): UIViewController {
    val platformContext = PlatformContext()
    val database = createAppDatabase(platformContext)
    val datastore = DefaultQuizDatastore(database.userStatsDao())
    val questionLoader = DefaultQuestionLoader(platformContext)
    val viewModel = QuizViewModel(datastore, questionLoader)

    val billingManager = BillingManager(platformContext) { tier ->
        viewModel.updateSubscriptionTier(tier)
    }
    billingManager.initialize()

    val platformActions = IosQuizPlatformActions(billingManager)

    return ComposeUIViewController {
        CompositionLocalProvider(LocalQuizPlatformActions provides platformActions) {
            PgdQuizTheme(quizType = QuizType.DEFAULT) {
                QuizMainScreen(
                    viewModel = viewModel,
                    onQuizTypeSelected = {}
                )
            }
        }
    }
}
