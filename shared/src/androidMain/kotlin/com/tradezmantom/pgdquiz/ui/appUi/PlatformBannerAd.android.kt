package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.BannerAd

private fun BannerAdPlacement.androidAdUnitId(): String = when (this) {
    BannerAdPlacement.QUIZ_SCREEN -> "ca-app-pub-7804898639422438/9682004736"
    // TODO: replace with real AdMob banner ad unit IDs for these placements once
    // created in AdMob. This is Google's shared test banner ID so both placements
    // work out of the box.
    BannerAdPlacement.LIVES_LOST -> "ca-app-pub-3940256099942544/6300978111"
    BannerAdPlacement.QUIZ_COMPLETED -> "ca-app-pub-3940256099942544/6300978111"
}

@Composable
actual fun PlatformBannerAd(placement: BannerAdPlacement, modifier: Modifier) {
    BannerAd(adUnitId = placement.androidAdUnitId(), modifier = modifier)
}
