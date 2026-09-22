@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)

package com.tradezmantom.pgdquiz.ui.appUi.mainScreen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tradezmantom.pgdquiz.ui.appUi.LocalQuizPlatformActions
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.ExamLayout
import com.tradezmantom.pgdquiz.ui.appUi.quizModeScreen.QuizModeSelection
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.ui.data.hasNoAds
import com.tradezmantom.pgdquiz.ui.data.hasUnlimitedLives
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel
import com.tradezmantom.pgdquiz.ui.theme.PgdQuizTheme
import org.jetbrains.compose.resources.painterResource
import pgdquiz.shared.generated.resources.Res
import pgdquiz.shared.generated.resources.droplet
import pgdquiz.shared.generated.resources.happypoo2
import pgdquiz.shared.generated.resources.neonsign2
import pgdquiz.shared.generated.resources.neonsign3
import pgdquiz.shared.generated.resources.pressure


@Composable
fun QuizMainScreen(
    viewModel: QuizViewModel,
    onQuizTypeSelected: (QuizType) -> Unit,
) {
    val platformActions = LocalQuizPlatformActions.current
    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    var selectedQuizType by rememberSaveable { mutableStateOf<QuizType?>(null) }
    var selectedMode by rememberSaveable { mutableStateOf<QuizDifficulty?>(null) }
    // Tracks the last quiz type actually chosen (independent of selectedQuizType, which gets
    // reset to null whenever the user backs out to the type-selection screen) so a switch
    // between two real quiz types — e.g. Plumbing -> Drainlaying — can be detected and shown
    // an interstitial. Switching in from having no prior selection (previous == null) never
    // triggers this, so a first-ever pick doesn't show an ad.
    var lastChosenQuizType by rememberSaveable { mutableStateOf<QuizType?>(null) }

    fun applySelectedQuizType(quizType: QuizType) {
        selectedQuizType = quizType
        onQuizTypeSelected(quizType)
    }


    PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                contentAlignment = Alignment.Center

            )
            { CircularProgressIndicator() }
        }
        when {
            selectedQuizType == null -> {
                QuizTypeSelection(
                    onSelectQuizType = { quizType ->
                        val previous = lastChosenQuizType
                        lastChosenQuizType = quizType
                        val isSwitchBetweenQuizTypes = previous != null &&
                                previous != QuizType.DEFAULT &&
                                previous != quizType
                        if (isSwitchBetweenQuizTypes && !state.hasNoAds()) {
                            platformActions.showQuizTypeChangeInterstitial {
                                applySelectedQuizType(quizType)
                            }
                        } else {
                            applySelectedQuizType(quizType)
                        }
                    },
                    onQuizTypeSelected = onQuizTypeSelected,

                    tradeTom = painterResource(Res.drawable.neonsign3)
                )
            }

            selectedMode == null -> {
                BackHandler {
                    selectedQuizType = null
                }
                LaunchedEffect(Unit) {
                    viewModel.refreshForDifficultyScreen()
                }
                QuizModeSelection(
                    quizType = selectedQuizType!!,
                    onSelectDifficulty = { mode ->
                        selectedMode = mode
                        viewModel.startQuiz(mode, selectedQuizType!!)
                    },
                    onBackToQuizType = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    lives = state.getCurrentQuizTypeLives(),
                    streak = state.answerStreak,
                    showAds = !state.hasNoAds(),
                    hasUnlimitedLives = state.hasUnlimitedLives(),
                    onUpgrade = {
                        platformActions.launchApprenticeSubscription()
                    })
            }

            else -> {
                BackHandler {
                    selectedMode = null
                }
                ExamLayout(
                    viewModel = viewModel,
                    onExit = {
                        selectedMode = null
                        selectedQuizType = null
                    },
                    quizDifficulty = selectedMode!!,
                    quizType = selectedQuizType!!,
                    examEmoji = painterResource(
                        when (selectedQuizType ?: QuizType.DEFAULT) {
                            QuizType.DRAIN_LAYING -> Res.drawable.happypoo2
                            QuizType.PLUMBING -> Res.drawable.droplet
                            QuizType.GASFITTING -> Res.drawable.pressure
                            QuizType.DEFAULT -> Res.drawable.neonsign2
                        }
                    ),
                    title = when (selectedQuizType ?: QuizType.DEFAULT) {
                        QuizType.DRAIN_LAYING -> "HappyPoo"
                        QuizType.PLUMBING -> "Droplet"
                        QuizType.GASFITTING -> "Pressure"
                        QuizType.DEFAULT -> "Default"
                    },
                    examCont = selectedQuizType!!.name,
                    onBackToModeSelect = {
                        selectedMode = null
                    }
                )
            }
        }
    }
}