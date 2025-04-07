package com.example.pgdquiz.ui

//import com.example.pgdquiz.ui.Composables.BannerAd
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.ui.Banner
import com.example.pgdquiz.ui.ui.ButtonGrid
import com.example.pgdquiz.ui.ui.LivesLost
import com.example.pgdquiz.ui.ui.QuestionField

@Composable
fun DrainLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel,
    onExit: () -> Unit

) {
    val lives = viewModel.lives.value


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Banner(
                examType = "Drainlaying",
                examEmoji = painterResource(R.drawable.happypoo),
                emojiCont = "happyPoo",
                attempts = viewModel.lives.value,
                icon = painterResource(R.drawable.flame1),
                streak = "Streak Count",
                streakCount = viewModel.streakCount.value,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            QuestionField(
                question = viewModel.currentQuestion,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
            ButtonGrid(viewModel = viewModel)
//        BannerAd()
        }
        if (lives <= 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp)
                ) {
                    LivesLost(
                        onWatchAd = {
                            viewModel.restoreLife()
                        },
                        onExit = onExit
                    )
                }
            }
        }
    }
}

