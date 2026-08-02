package com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.tradezmantom.pgdquiz.MainActivity
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.BannerAd
import com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen.landscape.ModeButtonLandscape
import com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen.portrait.ModeButtonPortrait
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.Banner
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty

import com.tradezmantom.pgdquiz.ui.data.QuizType

@Composable
fun QuizModeSelection(
    quizType: QuizType,
    onBackToQuizType: () -> Unit,
    lives: Int,
    streak: Int,
    onUpgrade: () -> Unit = {},
    hasUnlimitedLives: Boolean = false,
    onSelectDifficulty: (mode: QuizDifficulty) -> Unit,
    showAds: Boolean = true,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Banner(
                quizType = quizType,
                emojiCont = "Exam Icon",
                attempts = lives,
                streakCount = streak,
                modifier = Modifier.fillMaxWidth(),
                hasUnlimitedLives = hasUnlimitedLives,
                isPremium = !showAds,
                onBack = onBackToQuizType,
                onUpgrade = onUpgrade
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModeButtonLandscape(
                    label = "Easy",
                    questionCountText = "(25 Questions)",
                    onClick = { onSelectDifficulty(QuizDifficulty.EASY) },
                    quizType = quizType,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
                ModeButtonLandscape(
                    label = "Medium",
                    questionCountText = "(50 Questions)",
                    onClick = { onSelectDifficulty(QuizDifficulty.MEDIUM) },
                    quizType = quizType,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
                ModeButtonLandscape(
                    label = "Hard",
                    questionCountText = "(100 Questions)",
                    onClick = { onSelectDifficulty(QuizDifficulty.HARD) },
                    quizType = quizType,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (showAds) {
                    BannerAd(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        }
    }

                else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Banner(
                quizType = quizType,
                emojiCont = "Exam Icon",
                attempts = lives,
                streakCount = streak,
                modifier = Modifier.fillMaxWidth(),
                hasUnlimitedLives = hasUnlimitedLives,
                isPremium = !showAds,
                onBack = onBackToQuizType,
                onUpgrade = onUpgrade
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ModeButtonPortrait(
                    label = "Easy",
                    questionCountText = "(25 Questions)",
                    quizType = quizType,
                    onClick = { onSelectDifficulty(QuizDifficulty.EASY) }
                )
                ModeButtonPortrait(
                    label = "Medium",
                    questionCountText = "(50 Questions)",
                    quizType = quizType,
                    onClick = { onSelectDifficulty(QuizDifficulty.MEDIUM) }
                )
                ModeButtonPortrait(
                    label = "Hard",
                    questionCountText = "(100 Questions)",
                    quizType = quizType,
                    onClick = { onSelectDifficulty(QuizDifficulty.HARD) }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (showAds) {
                    BannerAd(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        }
    }
}