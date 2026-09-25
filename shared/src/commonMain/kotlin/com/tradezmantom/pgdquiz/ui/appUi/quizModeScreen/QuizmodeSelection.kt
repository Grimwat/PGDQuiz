package com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen

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
import androidx.compose.ui.unit.dp
import com.tradezmantom.pgdquiz.ui.appUi.BannerAdPlacement
import com.tradezmantom.pgdquiz.ui.appUi.PlatformBannerAd
import com.tradezmantom.pgdquiz.ui.appUi.forQuizType
import com.tradezmantom.pgdquiz.ui.appUi.isLandscapeOrientation
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
    // Real run length per difficulty (a small question bank caps Hard below 100).
    questionCount: (QuizDifficulty) -> Int = { it.questionCount },
    // "Exam Ready" (the full-length HARD run) is for paid tiers; free users see it locked
    // and tapping it opens the Apprentice purchase via onUpgrade.
    examReadyUnlocked: Boolean = true,
) {
    val isLandscape = isLandscapeOrientation()

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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModeButtonLandscape(
                    label = "Easy",
                    questionCountText = "(${questionCount(QuizDifficulty.EASY)} Questions)",
                    onClick = { onSelectDifficulty(QuizDifficulty.EASY) },
                    quizType = quizType,
                    difficulty = QuizDifficulty.EASY,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
                ModeButtonLandscape(
                    label = "Medium",
                    questionCountText = "(${questionCount(QuizDifficulty.MEDIUM)} Questions)",
                    onClick = { onSelectDifficulty(QuizDifficulty.MEDIUM) },
                    quizType = quizType,
                    difficulty = QuizDifficulty.MEDIUM,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
                ModeButtonLandscape(
                    label = "Exam Ready",
                    questionCountText = null,
                    locked = !examReadyUnlocked,
                    onClick = { if (examReadyUnlocked) onSelectDifficulty(QuizDifficulty.HARD) else onUpgrade() },
                    quizType = quizType,
                    difficulty = QuizDifficulty.HARD,
                    modifier = Modifier.weight(1f).fillMaxHeight(0.7f)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (showAds) {
                    PlatformBannerAd(
                        placement = BannerAdPlacement.forQuizType(quizType),
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
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ModeButtonPortrait(
                    label = "Easy",
                    questionCountText = "(${questionCount(QuizDifficulty.EASY)} Questions)",
                    quizType = quizType,
                    difficulty = QuizDifficulty.EASY,
                    onClick = { onSelectDifficulty(QuizDifficulty.EASY) }
                )
                ModeButtonPortrait(
                    label = "Medium",
                    questionCountText = "(${questionCount(QuizDifficulty.MEDIUM)} Questions)",
                    quizType = quizType,
                    difficulty = QuizDifficulty.MEDIUM,
                    onClick = { onSelectDifficulty(QuizDifficulty.MEDIUM) }
                )
                ModeButtonPortrait(
                    label = "Exam Ready",
                    questionCountText = null,
                    locked = !examReadyUnlocked,
                    quizType = quizType,
                    difficulty = QuizDifficulty.HARD,
                    onClick = { if (examReadyUnlocked) onSelectDifficulty(QuizDifficulty.HARD) else onUpgrade() }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (showAds) {
                    PlatformBannerAd(
                        placement = BannerAdPlacement.forQuizType(quizType),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            }
        }
    }
}