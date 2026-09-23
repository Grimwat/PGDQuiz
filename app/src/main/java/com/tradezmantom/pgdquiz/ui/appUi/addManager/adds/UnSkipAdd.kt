package com.tradezmantom.pgdquiz.ui.appUi.addManager.adds

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UnSkipAddManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var quizTypeChangeInterstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false
    private var isRewardedLoading = false
    private var isQuizTypeChangeInterstitialLoading = false

    private val _rewardedAdReady = MutableStateFlow(false)
    val rewardedAdReady: StateFlow<Boolean> = _rewardedAdReady.asStateFlow()

    fun loadAllAds() {
        loadInterstitial()
        loadRewarded()
        loadQuizTypeChangeInterstitial()
    }


    private fun loadInterstitial() {
        if (isInterstitialLoading || interstitialAd != null) return
        isInterstitialLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-7804898639422438/3140647116",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false
                    Log.d("ADS", "Interstitial loaded successfully")
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                    Log.e(
                        "ADS",
                        "Interstitial failed to load: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                }
            }
        )
    }

    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial()
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    Log.e(
                        "ADS",
                        "Interstitial failed to show: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                    loadInterstitial()
                    onAdDismissed()
                }
            }
            interstitialAd?.show(activity)
        } else {
            Log.d("ADS", "Interstitial not ready when requested, skipping")
            onAdDismissed()
            loadInterstitial()
        }
    }


    private fun loadQuizTypeChangeInterstitial() {
        if (isQuizTypeChangeInterstitialLoading || quizTypeChangeInterstitialAd != null) return
        isQuizTypeChangeInterstitialLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-7804898639422438/3454363490",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    quizTypeChangeInterstitialAd = ad
                    isQuizTypeChangeInterstitialLoading = false
                    Log.d("ADS", "Quiz type change interstitial loaded successfully")
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    quizTypeChangeInterstitialAd = null
                    isQuizTypeChangeInterstitialLoading = false
                    Log.e(
                        "ADS",
                        "Quiz type change interstitial failed to load: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                }
            }
        )
    }

    fun showQuizTypeChangeInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = quizTypeChangeInterstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    quizTypeChangeInterstitialAd = null
                    loadQuizTypeChangeInterstitial()
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    quizTypeChangeInterstitialAd = null
                    Log.e(
                        "ADS",
                        "Quiz type change interstitial failed to show: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                    loadQuizTypeChangeInterstitial()
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } else {
            Log.d("ADS", "Quiz type change interstitial not ready when requested, skipping")
            onAdDismissed()
            loadQuizTypeChangeInterstitial()
        }
    }


    private fun loadRewarded() {
        if (isRewardedLoading || rewardedAd != null) return
        isRewardedLoading = true
        _rewardedAdReady.value = false
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            "ca-app-pub-7804898639422438/1149028735",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isRewardedLoading = false
                    _rewardedAdReady.value = true
                    Log.d("ADS", "Rewarded ad loaded successfully")
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    isRewardedLoading = false
                    _rewardedAdReady.value = false
                    Log.e(
                        "ADS",
                        "Rewarded ad failed to load: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                }
            }
        )
    }


    fun showRewarded(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdUnavailable: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    _rewardedAdReady.value = false
                    loadRewarded()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    _rewardedAdReady.value = false
                    Log.e(
                        "ADS",
                        "Rewarded ad failed to show: code=${adError.code} " +
                                "domain=${adError.domain} message=${adError.message}"
                    )
                    loadRewarded()
                    onAdUnavailable()
                }
            }
            ad.show(activity) { _ ->
                onRewardEarned()
            }
        } else {

            Log.d("ADS", "Rewarded ad not ready when requested, no reward granted")
            loadRewarded()
            onAdUnavailable()
        }
    }
}