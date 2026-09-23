package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.BannerAd

private fun BannerAdPlacement.androidAdUnitId(): String = when (this) {
    BannerAdPlacement.QUIZ_SCREEN -> "ca-app-pub-7804898639422438/9682004736"
    BannerAdPlacement.LIVES_LOST -> "ca-app-pub-7804898639422438/6248047797"
    BannerAdPlacement.QUIZ_COMPLETED -> "ca-app-pub-7804898639422438/2141281820"
    BannerAdPlacement.WELCOME -> "ca-app-pub-7804898639422438/7177986089"
}

@Composable
actual fun PlatformBannerAd(placement: BannerAdPlacement, modifier: Modifier) {
    BannerAd(adUnitId = placement.androidAdUnitId(), modifier = modifier)
}
