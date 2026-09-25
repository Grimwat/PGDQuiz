package com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Second line of a difficulty button: the question count, or for a paid-only mode shown to a
 * free user, a lock with an APPRENTICE tag. Draws nothing when there's neither.
 */
@Composable
internal fun ModeButtonSubtitle(questionCountText: String?, locked: Boolean) {
    if (locked) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(text = "🔒", fontSize = 13.sp)
            Text(
                text = "APPRENTICE",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    } else if (questionCountText != null) {
        Text(
            text = questionCountText,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp
        )
    }
}
