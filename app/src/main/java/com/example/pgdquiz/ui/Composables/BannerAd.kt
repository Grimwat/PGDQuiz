package com.example.pgdquiz.ui.Composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd (modifier: Modifier = Modifier) {
AndroidView(
    modifier = Modifier
        .fillMaxWidth(),
    factory = {
    val windowMetrics: WindowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(it)
    val bounds = windowMetrics.bounds

    var adWidthPixels = it.resources.displayMetrics.widthPixels.toFloat()

    if (adWidthPixels == 0F) {
        adWidthPixels = bounds.width().toFloat()
    }

    val density = it.resources.displayMetrics.density
    val adWith = (adWidthPixels / density). toInt()

    AdView(it).apply {
        setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(it, adWith))
        adUnitId = "ca-app-pub-3940256099942544/9214589741"
        loadAd(AdRequest.Builder().build())
    }

})
}