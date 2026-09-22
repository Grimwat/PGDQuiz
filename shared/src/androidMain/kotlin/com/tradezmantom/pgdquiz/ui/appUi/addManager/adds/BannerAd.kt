package com.tradezmantom.pgdquiz.ui.appUi.addManager.adds

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAd(adUnitId: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = {
            val windowMetrics: WindowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(it)
            val bounds = windowMetrics.bounds

            var adWidthPixels = it.resources.displayMetrics.widthPixels.toFloat()

            if (adWidthPixels == 0F) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = it.resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            AdView(it).apply {
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(it, adWidth))
                this.adUnitId = adUnitId

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d("ADS", "Banner ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e(
                            "ADS",
                            "Banner ad failed to load: code=${adError.code} " +
                                    "domain=${adError.domain} message=${adError.message}"
                        )
                    }

                    override fun onAdOpened() {
                        Log.d("ADS", "Banner ad opened")
                    }

                    override fun onAdClicked() {
                        Log.d("ADS", "Banner ad clicked")
                    }
                }

                loadAd(AdRequest.Builder().build())
            }
        },
        onRelease = { adView ->
            adView.destroy()
        }
    )
}