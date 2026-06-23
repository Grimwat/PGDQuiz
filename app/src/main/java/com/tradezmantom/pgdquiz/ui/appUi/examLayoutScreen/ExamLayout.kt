package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen


import androidx.compose.ui.platform.LocalContext
import com.tradezmantom.pgdquiz.MainActivity
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.BannerAd
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.RewardAdSuccess
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.landscape.LandscapeGrid
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays.CongratulationsScreen
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays.LivesLost
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.portrait.ButtonsPortrait
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuestions
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.ui.data.hasNoAds
import com.tradezmantom.pgdquiz.ui.data.hasUnlimitedLives
import com.tradezmantom.pgdquiz.ui.data.isPremium
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel


@Composable
fun ExamLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel(),
    onExit: () -> Unit,
    examEmoji: Painter,
    title: String,
    examCont: String,
    quizDifficulty: QuizDifficulty,
    quizType: QuizType,
    onBackToModeSelect: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    val showAds = !state.hasNoAds()

    LaunchedEffect(quizType, quizDifficulty) {
        viewModel.startQuiz(quizDifficulty, quizType)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.navigationBars.asPaddingValues()),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.currentQuestion != null) {
                if (isLandscape) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Banner(
                                quizType = quizType,
                                emojiCont = examCont,
                                attempts = state.getCurrentQuizTypeLives(),
                                streakCount = state.answerStreak,
                                modifier = Modifier.fillMaxWidth(),
                                hasUnlimitedLives = state.hasUnlimitedLives(),
                                isPremium = state.isPremium(),
                                onBack = { onBackToModeSelect() },
                                onUpgrade = {
                                    (context as? MainActivity)?.launchApprenticeSubscription()
                                }
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                QuestionField(
                                    question = state.currentQuestion,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    quizType = quizType
                                )
                                LandscapeGrid(
                                    viewModel = viewModel,
                                    quizType = quizType,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                )
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
                    }

                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Banner(
                                quizType = quizType,
                                emojiCont = examCont,
                                attempts = state.getCurrentQuizTypeLives(),
                                streakCount = state.answerStreak,
                                modifier = Modifier.fillMaxWidth(),
                                hasUnlimitedLives = state.hasUnlimitedLives(),
                                isPremium = state.isPremium(),
                                onBack = { onBackToModeSelect() },
                                onUpgrade = {
                                    (context as? MainActivity)?.launchApprenticeSubscription()
                                }
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            QuestionField(
                                question = state.currentQuestion,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp),
                                quizType = quizType
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            ButtonsPortrait(
                                viewModel = viewModel,
                                quizType = quizType,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                            )
                        }

                        NextButton(
                            onClick = {
                                val totalQuestions = state.getCurrentQuestions().size
                                val activity = context as? MainActivity

                                if (viewModel.canShowAd() && showAds) {
                                    activity?.showInterstitialAd {
                                        viewModel.triggerShowCorrectAnswer()
                                    }
                                } else {
                                    viewModel.triggerShowCorrectAnswer()
                                }
                            },
                            quizType = quizType,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )

                        if (showAds) {
                            BannerAd(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
        }

        if (state.getCurrentQuizTypeLives() <= 0 && !state.hasUnlimitedLives()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    LivesLost(
                        onWatchAd = if (showAds) {{
                            (context as? MainActivity)?.showRewardedAd {
                                viewModel.handleRewardEarned()
                            }
                        }} else null,
                        onExit = onExit,
                        examEmoji = examEmoji,
                        emojiCont = title,
                        quizType = quizType,
                        onUpgradeApprentice = {
                            (context as? MainActivity)?.launchApprenticeSubscription()
                        },
                        onUpgradeTradesman = {
                            (context as? MainActivity)?.launchTradesmanPurchase()
                        }
                    )
                }
            }
        }

        if (state.showCongratulationsScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    CongratulationsScreen(
                        examEmoji = examEmoji,
                        emojiCont = title,
                        onRestart = {
                            if (showAds) {
                                (context as? MainActivity)?.showInterstitialAd {
                                    viewModel.startQuiz(quizDifficulty, quizType)
                                }
                            } else {
                                viewModel.startQuiz(quizDifficulty, quizType)
                            }
                        },
                        onBackToModeSelect = {
                            if (showAds) {
                                (context as? MainActivity)?.showInterstitialAd {
                                    onBackToModeSelect()
                                }
                            } else {
                                onBackToModeSelect()
                            }
                        },
                        quizType = quizType
                    )
                }
            }
        }

        if (state.isShowingRewardSuccess) {
            RewardAdSuccess(
                onDismiss = { viewModel.dismissRewardSuccess() }
            )
        }
    }
}