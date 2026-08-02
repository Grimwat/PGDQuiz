package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.R
import com.tradezmantom.pgdquiz.ui.data.QuizType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NextButton(
    onClick: () -> Unit,
    quizType: QuizType,
    modifier: Modifier = Modifier
) {
    var isClickable by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val examId = when (quizType) {
        QuizType.GASFITTING -> R.drawable.gasslogo
        QuizType.PLUMBING -> R.drawable.plumblogo
        QuizType.DRAIN_LAYING -> R.drawable.drainlogo
        QuizType.DEFAULT -> null
    }

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.outline,
            MaterialTheme.colorScheme.primary
        )
    )

    val glowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    onClick()
                    scope.launch {
                        delay(1500L)
                        isClickable = true
                    }
                }
            },
            enabled = isClickable,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Unspecified
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = glowColor,
                    spotColor = glowColor
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isClickable) listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.colorScheme.primary
                            ) else listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.outline
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                examId?.let { id ->
                    Image(
                        painter = painterResource(id),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.15f),
                    )
                }
                Text(
                    text = if (isClickable) "Next Question" else "...",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}