package com.tradezmantom.pgdquiz.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// No Material You / dynamic color on iOS.
@Composable
internal actual fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme? = null
