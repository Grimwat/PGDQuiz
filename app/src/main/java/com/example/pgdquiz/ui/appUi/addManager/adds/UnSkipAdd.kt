package com.example.pgdquiz.ui.appUi.addManager.adds

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class UnSkipAddManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    fun loadAllAds() {
        loadInterstitial()
        loadRewarded()
    }


    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context.applicationContext,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    android.util.Log.e("AdMob", "Interstitial failed: ${adError.message}")
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
                    loadInterstitial()
                    onAdDismissed()
                }
            }
            interstitialAd?.show(activity)
        } else {
            onAdDismissed()
            loadInterstitial()
        }
    }


    private fun loadRewarded() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context.applicationContext,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    fun showRewarded(activity: Activity, onRewardEarned: () -> Unit) {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    loadRewarded()
                }
            }
            rewardedAd?.show(activity) { _ ->
                onRewardEarned()
            }
        } else {

            onRewardEarned()
            loadRewarded()
        }
    }
}