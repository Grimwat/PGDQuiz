package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tradezmantom.pgdquiz.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tradezmantom.pgdquiz.ui.data.QuizType
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    emojiCont: String,
    attempts: Int,
    streakCount: Int,
    hasUnlimitedLives: Boolean = false,
    isPremium: Boolean = false,
    onBack: () -> Unit,
    onUpgrade: (() -> Unit)? = null
) {
    val (title, examEmoji) = when (quizType) {
        QuizType.DRAIN_LAYING -> "Drainlaying" to R.drawable.drainlogo
        QuizType.PLUMBING -> "Plumbing" to R.drawable.plumblogo
        QuizType.GASFITTING -> "Gasfitting" to R.drawable.gasslogo
        QuizType.DEFAULT -> "Quiz App" to R.drawable.arrow
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val verticalPadding = if (isLandscape) 6.dp else 10.dp
    val iconSize = if (isLandscape) 24.dp else 28.dp
    val glowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.primary
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = verticalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(iconSize),
                        tint = Color.White
                    )
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.flame1),
                        contentDescription = "Streak",
                        modifier = Modifier
                            .size(iconSize)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = streakCount.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(examEmoji),
                        contentDescription = emojiCont,
                        modifier = Modifier
                            .size(iconSize)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = if (hasUnlimitedLives) "∞" else attempts.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            if (!isPremium && onUpgrade != null) {
                Button(
                    onClick = onUpgrade,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡ Upgrade to remove ads & unlimited lives",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun Banner(

) {

}
