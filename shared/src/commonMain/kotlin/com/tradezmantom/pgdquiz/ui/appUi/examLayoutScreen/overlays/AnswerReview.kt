package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.ui.data.AnsweredQuestion

private val CorrectColor = Color(0xFF4CD964)
private val WrongColor = Color(0xFFFF5A5A)

/**
 * "Review answers" entry point shown on the lives-lost and congratulations overlays.
 * Paid users open the review; free users see it locked as an Apprentice teaser.
 */
@Composable
fun ReviewAnswersButton(
    answered: List<AnsweredQuestion>,
    unlocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Styled to match the overlays' Restart / Leave Quiz buttons.
    Button(
        onClick = onClick,
        enabled = answered.isNotEmpty(),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${if (unlocked) "📋" else "🔒"} Review answers",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    // With large system fonts, shrink the label rather than wrapping the tag.
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (!unlocked) {
                    Text(
                        text = "APPRENTICE",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/** Full-screen, in-order list of every answer from the current run. */
@Composable
fun AnswerReviewScreen(
    answered: List<AnsweredQuestion>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val correct = answered.count { it.isCorrect }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.background)
                )
            )
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Your answers",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "$correct of ${answered.size} correct",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(answered) { index, item ->
                AnswerReviewCard(number = index + 1, item = item)
            }
        }
    }
}

@Composable
private fun AnswerReviewCard(number: Int, item: AnsweredQuestion) {
    val accent = if (item.isCorrect) CorrectColor else WrongColor
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .border(1.5.dp, accent.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Q$number",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accent,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = if (item.isCorrect) "Correct" else "Incorrect",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = accent
            )
        }
        Text(
            text = item.question,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        if (item.isCorrect) {
            AnswerLine(mark = "✓", label = null, text = item.correctAnswer, color = CorrectColor)
        } else {
            AnswerLine(
                mark = "✗",
                label = "Your answer",
                // A timed-out/blank submission records an empty selection.
                text = item.selectedAnswer.ifBlank { "No answer" },
                color = WrongColor
            )
            AnswerLine(mark = "✓", label = "Correct answer", text = item.correctAnswer, color = CorrectColor)
        }
    }
}

@Composable
private fun AnswerLine(mark: String, label: String?, text: String, color: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(text = mark, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        Column {
            if (label != null) {
                Text(text = label, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(text = text, color = Color.White, fontSize = 14.sp)
        }
    }
}
