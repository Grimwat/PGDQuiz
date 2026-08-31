package com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen.landscape

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.theme.BackgroundLogo

private fun difficultyColors(difficulty: QuizDifficulty): Pair<Color, Color> {
    return when (difficulty) {
        QuizDifficulty.EASY -> Color(0xFF1B6B1B) to Color(0xFF3DDC3D)
        QuizDifficulty.MEDIUM -> Color(0xFF8A7A0A) to Color(0xFFE0C020)
        QuizDifficulty.HARD -> Color(0xFF7A1414) to Color(0xFFE0303A)
    }
}

@Composable
fun ModeButtonLandscape(
    label: String,
    questionCountText: String,
    quizType: QuizType,
    difficulty: QuizDifficulty,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (fillColor, glowColor) = difficultyColors(difficulty)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(90.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = glowColor.copy(alpha = 0.6f),
                spotColor = glowColor.copy(alpha = 0.6f)
            )
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(glowColor, glowColor.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = fillColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            BackgroundLogo(
                quizType = quizType,
                modifier = Modifier.alpha(0.12f)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = questionCountText,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}