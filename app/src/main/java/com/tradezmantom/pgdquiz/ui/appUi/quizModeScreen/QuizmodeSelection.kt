package com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
    showAds: Boolean = true
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ModeButtonLandscape(
                        label = "Easy",
                        questionCountText = "(25 Questions)",
                        onClick = { onSelectDifficulty(QuizDifficulty.EASY) },
                        quizType = quizType,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ModeButtonLandscape(
                        label = "Medium",
                        questionCountText = "(50 Questions)",
                        onClick = { onSelectDifficulty(QuizDifficulty.MEDIUM) },
                        quizType = quizType,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ModeButtonLandscape(
                        label = "Hard",
                        questionCountText = "(100 Questions)",
                        onClick = { onSelectDifficulty(QuizDifficulty.HARD) },
                        quizType = quizType,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (showAds) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BannerAd(
                        modifier = Modifier
                            .width(160.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
                    .padding(horizontal = 24.dp, vertical = 200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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

            if (showAds) {
                BannerAd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .navigationBarsPadding()
                )
            }
        }
    }
}