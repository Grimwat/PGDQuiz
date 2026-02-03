package com.example.pgdquiz.ui.appUi.examLayoutScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.data.QuizType
import kotlin.text.forEach

@Composable
fun NextButton(
    onClick: () -> Unit,
    quizType: QuizType,
    modifier: Modifier = Modifier
) {
    val examId = when (quizType) {
        QuizType.GASFITTING -> R.drawable.gasslogo
        QuizType.PLUMBING -> R.drawable.plumblogo
        QuizType.DRAIN_LAYING -> R.drawable.drainlogo
        QuizType.DEFAULT -> null
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified
            ),
            modifier = Modifier.background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .border(width = 4.dp, color = MaterialTheme.colorScheme.outline)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                examId?.let { id ->
                    Image(
                        painter = painterResource(id),
                        contentDescription = null,
                        modifier = Modifier
                            .alpha(0.2f),
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                    }
                }
                        Text(
                            text = "Next Question",
                            color = Color.White,
                            fontWeight = Bold
                        )
                        }
                    }
                    }
                }

