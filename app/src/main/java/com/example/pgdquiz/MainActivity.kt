package com.example.pgdquiz


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.domain.DefaultQuestionLoader
import com.example.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.logic.QuizViewModel
import com.example.pgdquiz.ui.logic.QuizViewModelFactory
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : ComponentActivity() {

    private var selectedQuizType by mutableStateOf<QuizType?>(null)
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd : RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MobileAds.initialize(this) {}
        loadRewardedAd( )
        setContent {
            val app = application as QuizApplication

            val questionLoader = DefaultQuestionLoader(context = applicationContext)

            val viewModel: QuizViewModel = viewModel(
                factory = QuizViewModelFactory(
                    quizDatastore = app.quizDatastore,
                    questionLoader = DefaultQuestionLoader(context = applicationContext)
                )
            )

            PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
                QuizMainScreen(
                    viewModel = viewModel,
                    onQuizTypeSelected = { selectedQuizType = it }
                )
            }
        }
    }
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) { interstitialAd = ad }
                override fun onAdFailedToLoad(error: LoadAdError) { interstitialAd = null }
            })
    }

    fun showInterstitialAd(onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object :
                FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdDismissed()
                }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onAdDismissed()
                }
            }
            interstitialAd?.show(this)
        } else {
            onAdDismissed()
            loadInterstitialAd()
        }
    }
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            "ca-app-pub-7804898639422438/6470745786",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }
    fun showRewardedAd(onRewardEarned: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(this) { _ ->
                onRewardEarned()
                loadRewardedAd()
            }
        } ?: run {
            onRewardEarned()
            loadRewardedAd()
        }
    }
}