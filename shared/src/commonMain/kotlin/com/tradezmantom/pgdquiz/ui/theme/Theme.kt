package com.tradezmantom.pgdquiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.tradezmantom.pgdquiz.ui.data.QuizType

private val DrainDarkScheme = darkColorScheme(
    primary = DarkRed,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = LightRed,
)
private val DrainLightScheme = lightColorScheme(
    primary = LightRed,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = DarkRed,
)
private val DarkColorScheme = darkColorScheme (
    primary = Black,
    secondary = Blue,
    onSurface = DarkBLue,
    onBackground = Gas,
    onPrimary = Yellow,
    tertiary = Grey,
    background = LightRed,
    surface = DarkGrey,
    outline = DarkRed,
)
private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = Blue,
    onSurface = DarkBLue,
    onBackground = Gas,
    onPrimary = Yellow,
    tertiary = Grey,
    background = LightRed,
    surface = DarkGrey,
    outline = DarkRed,
)
private val GasfittingDarkColorScheme = darkColorScheme (
    primary = Gas,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = Yellow,
    )
private val GasfittingColorScheme = lightColorScheme(
    primary = Gas,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = Yellow,
)
private val PlumbingDarkColorScheme = darkColorScheme (
    primary = DarkBLue,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = Blue,
    )
private val PlumbingColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = DarkBLue,
)

// Dynamic color (Material You) is an Android 12+ OS feature with no iOS equivalent.
// This returns null on platforms/OS versions where it isn't available.
@Composable
internal expect fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme?

@Composable
fun PgdQuizTheme(
    quizType: QuizType,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val dynamicScheme = if (dynamicColor) platformDynamicColorScheme(darkTheme) else null

    val colorScheme = dynamicScheme ?: when {
        quizType == QuizType.GASFITTING && darkTheme -> GasfittingDarkColorScheme
        quizType == QuizType.GASFITTING -> GasfittingColorScheme

        quizType == QuizType.PLUMBING && darkTheme -> PlumbingDarkColorScheme
        quizType == QuizType.PLUMBING -> PlumbingColorScheme

        quizType == QuizType.DRAIN_LAYING && darkTheme -> DrainDarkScheme
        quizType == QuizType.DRAIN_LAYING -> DrainLightScheme

        quizType == QuizType.DEFAULT && darkTheme -> DarkColorScheme
        quizType == QuizType.DEFAULT -> LightColorScheme

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
