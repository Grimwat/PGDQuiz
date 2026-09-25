package com.tradezmantom.pgdquiz.ui.appUi.addManager.adds

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAd(adUnitId: String, modifier: Modifier = Modifier) {
    // Size the ad to the width it's actually given, not the full screen, so banners inside
    // padded overlay cards aren't clipped at the edges.
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val adWidth = maxWidth.value.toInt()
        key(adWidth) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    AdView(it).apply {
                        setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(it, adWidth))
                        this.adUnitId = adUnitId

                        adListener = object : AdListener() {
                            override fun onAdLoaded() {
                                Log.d("ADS", "Banner ad loaded successfully ($adUnitId)")
                            }

                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.e(
                                    "ADS",
                                    "Banner ad failed to load ($adUnitId): code=${adError.code} " +
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
    }
}
