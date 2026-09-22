package com.tradezmantom.pgdquiz


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tradezmantom.pgdquiz.billing.BillingManager
import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.billing.PurchaseLauncher
import com.tradezmantom.pgdquiz.domain.DefaultQuestionLoader
import com.tradezmantom.pgdquiz.ui.appUi.LocalQuizPlatformActions
import com.tradezmantom.pgdquiz.ui.appUi.QuizPlatformActions
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.UnSkipAddManager
import com.tradezmantom.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModelFactory
import com.tradezmantom.pgdquiz.ui.theme.PgdQuizTheme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity(), QuizPlatformActions {

    private var selectedQuizType by mutableStateOf<QuizType?>(null)
    private var adManager: UnSkipAddManager? = null
    private var billingManager: BillingManager? = null
    private var quizViewModel: QuizViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val app = application as QuizApplication

        billingManager = BillingManager(PlatformContext(this)) { tier ->
            quizViewModel?.updateSubscriptionTier(tier)
        }
        app.billingManager = billingManager!!
        billingManager?.initialize()
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        adManager = UnSkipAddManager(this)

        try {
            MobileAds.initialize(this) { _ ->
                window.decorView.postDelayed({
                    if (!isFinishing) {
                        Log.d("ADS", "Initial loading of ads started...")
                        adManager?.loadAllAds()
                    }
                }, 1500)
            }
        } catch (e: Exception) {
            Log.e("ADS_ERROR", "AdMob failed: ${e.message}")
        }

        setContent {
            val app = application as? QuizApplication

            if (app != null) {
                val viewModel: QuizViewModel = viewModel(
                    factory = QuizViewModelFactory(
                        quizDatastore = app.quizDatastore,
                        questionLoader = DefaultQuestionLoader(PlatformContext(this@MainActivity))
                    )
                )
                quizViewModel = viewModel

                CompositionLocalProvider(LocalQuizPlatformActions provides this@MainActivity) {
                    PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
                        QuizMainScreen(
                            viewModel = viewModel,
                            onQuizTypeSelected = { selectedQuizType = it }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: QuizApplication not recognized. Check Manifest.")
                }
                Log.e("FATAL", "QuizApplication cast failed. Is it in AndroidManifest?")
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        billingManager?.destroy()
    }

    override fun launchApprenticeSubscription() {
        billingManager?.launchApprenticeFlow(PurchaseLauncher(this))
    }

    override fun launchTradesmanPurchase() {
        billingManager?.launchTradesmanFlow(PurchaseLauncher(this))
    }
    override fun showInterstitialAd(onAdDismissed: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showInterstitial(this, onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    override fun showRewardedAd(onRewardEarned: () -> Unit, onAdUnavailable: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showRewarded(this, onRewardEarned, onAdUnavailable)
        } else {
            onAdUnavailable()
        }
    }

    override fun showQuizTypeChangeInterstitial(onAdDismissed: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showQuizTypeChangeInterstitial(this, onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    override fun showLivesLostInterstitial(onAdDismissed: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showLivesLostInterstitial(this, onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    override fun showCongratulationsInterstitial(onAdDismissed: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showCongratulationsInterstitial(this, onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    private val fallbackRewardedAdReady = MutableStateFlow(false)

    override val rewardedAdReady: StateFlow<Boolean>
        get() = adManager?.rewardedAdReady ?: fallbackRewardedAdReady
}