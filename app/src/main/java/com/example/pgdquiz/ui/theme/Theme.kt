package com.example.pgdquiz.ui.theme

import android.R.id.primary
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.pgdquiz.ui.QuizType

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
    primary = LightRed,
    secondary = Blue,
    onSurface = DarkBLue,
    onBackground = Yellow,
    onPrimary = Gas,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = DarkRed,
)
private val LightColorScheme = lightColorScheme(
    primary = LightRed,
    secondary = Blue,
    onSurface = DarkBLue,
    onBackground = Yellow,
    onPrimary = Gas,
    tertiary = Grey,
    background = Black,
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
    primary = Yellow,
    secondary = Green,
    tertiary = Grey,
    background = Black,
    surface = DarkGrey,
    outline = Gas,
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

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */


@Composable
fun PgdQuizTheme(
    quizType: QuizType,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        quizType == QuizType.GASFITTING && darkTheme -> GasfittingDarkColorScheme
        quizType == QuizType.GASFITTING -> GasfittingColorScheme

        quizType == QuizType.PLUMBING && darkTheme -> PlumbingDarkColorScheme
        quizType == QuizType.PLUMBING -> PlumbingColorScheme

        quizType == QuizType.DRAINLAYING && darkTheme -> DrainDarkScheme
        quizType == QuizType.DRAINLAYING -> DrainLightScheme


        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}