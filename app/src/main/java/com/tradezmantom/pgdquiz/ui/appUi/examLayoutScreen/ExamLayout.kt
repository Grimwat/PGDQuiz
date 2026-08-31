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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.BannerAd
import com.tradezmantom.pgdquiz.ui.appUi.addManager.adds.RewardAdSuccess
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.landscape.LandscapeGrid
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays.CongratulationsScreen
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.overlays.LivesLost
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.portrait.ButtonsPortrait
import com.tradezmantom.pgdquiz.ui.appUi.findActivity
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuestions
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.ui.data.hasNoAds
import com.tradezmantom.pgdquiz.ui.data.hasUnlimitedLives
import com.tradezmantom.pgdquiz.ui.data.isPremium
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel


@Composable
private fun QuestionProgressIndicator(
    currentIndex: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Question ${currentIndex + 1} of $totalQuestions",
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        modifier = modifier
    )
}

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
    val mainActivity = context as? MainActivity
    val rewardedAdReady by (mainActivity?.rewardedAdReady?.collectAsStateWithLifecycle(initialValue = false)
        ?: remember { mutableStateOf(false) })

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                                    (context.findActivity() as? MainActivity)?.launchApprenticeSubscription()
                                }
                            )
                            QuestionProgressIndicator(
                                currentIndex = state.currentQuestionIndex,
                                totalQuestions = state.getCurrentQuestions().size,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            QuestionField(
                                question = state.currentQuestion,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 12.dp),
                                quizType = quizType
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            LandscapeGrid(
                                viewModel = viewModel,
                                quizType = quizType,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                            )
                        }
                    }

                    if (showAds) {
                        BannerAd(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        )
                    }
                }
            }
            else {
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
                        QuestionProgressIndicator(
                            currentIndex = state.currentQuestionIndex,
                            totalQuestions = state.getCurrentQuestions().size,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
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
                            .padding(bottom = 8.dp)

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
                            (context as? MainActivity)?.showRewardedAd(
                                onRewardEarned = {
                                    viewModel.handleRewardEarned()
                                },
                                onAdUnavailable = {
                                    // Ad wasn't ready yet — no reward was granted.
                                    // Swap this Log line for a Snackbar/Toast if you
                                    // want to surface it to the user directly.
                                    android.util.Log.d("ADS", "Rewarded ad unavailable, no attempt granted")
                                }
                            )
                        }} else null,
                        watchAdEnabled = rewardedAdReady,
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
                        quizType = quizType,
                        totalQuestions = state.getCurrentQuestions().size,
                        correctAnswers = state.correctAnswersCount,
                        bestStreak = state.bestStreak,
                        isPremium = state.isPremium(),
                        onUpgrade = {
                            (context as? MainActivity)?.launchApprenticeSubscription()
                        },
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
                        }
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