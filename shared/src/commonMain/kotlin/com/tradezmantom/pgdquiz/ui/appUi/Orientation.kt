package com.tradezmantom.pgdquiz.ui.appUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo

/** Cross-platform replacement for reading `LocalConfiguration.current.orientation` (Android-only). */
@Composable
fun isLandscapeOrientation(): Boolean {
    val size = LocalWindowInfo.current.containerSize
    return size.width > size.height
}
