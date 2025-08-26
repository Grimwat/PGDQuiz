package com.example.pgdquiz.ui.AppUi.ExamLayoutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerButton(
    optionText: String,
    isSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier,
    isCorrect: Boolean,
    showCorrectAnswer: Boolean,
    shape: Shape
) {
    val backgroundBrush = when {
        showCorrectAnswer && isCorrect -> Brush.radialGradient(
            colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary),
            radius = 600f
        )

        isSelected -> Brush.radialGradient(
            colors = listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.primary),
            radius = 600f
        )

        else -> Brush.radialGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.background
            ),
            radius = 600f
        )
    }
    val fontSize = when {
        optionText.length > 100 -> 12.sp
        optionText.length > 60 -> 14.sp
        else -> 16.sp
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onButtonSelected,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified
            ),
            modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .wrapContentHeight()
            .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush = backgroundBrush)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionText,
                    color = Color.White,
                    fontSize = fontSize,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}