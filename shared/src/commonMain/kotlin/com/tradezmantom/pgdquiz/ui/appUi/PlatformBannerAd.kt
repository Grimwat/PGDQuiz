package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Renders a platform banner ad, or nothing where no ad SDK is wired up yet (iOS, for now). */
@Composable
expect fun PlatformBannerAd(modifier: Modifier = Modifier)
