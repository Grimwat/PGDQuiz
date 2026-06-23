package com.tradezmantom.pgdquiz.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.QuizUiState
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.ui.data.hasUnlimitedLives
import com.tradezmantom.pgdquiz.util.STARTING_ANSWER_STREAK
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizDatastore: QuizDatastore,
    private val questionLoader: QuestionLoader
) : ViewModel() {

    private val _quizUiState = MutableStateFlow(QuizUiState())
    val quizUiState: StateFlow<QuizUiState> = _quizUiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500L)
            initializeUserStats()
        }
    }


    fun startQuiz(difficulty: QuizDifficulty, quizType: QuizType) {

        viewModelScope.launch {
            _quizUiState.update { it.copy(
                isLoading = true,
                showCongratulationsScreen = false,
                quizComplete = false,
                currentQuestionIndex = 0
            ) }
            refreshStats()

            val currentState = _quizUiState.value
            val shouldLoadNewQuestions =
                currentState.questions.isEmpty() || quizType != currentState.quizType

            if (shouldLoadNewQuestions) {
                delay(50)

                val allQuestions = questionLoader.loadQuestions(quizType)
                val selectedQuestions = allQuestions.shuffled().take(
                    when (difficulty) {
                        QuizDifficulty.EASY -> 25
                        QuizDifficulty.MEDIUM -> 50
                        QuizDifficulty.HARD -> 100
                    }
                )

                if (selectedQuestions.isNotEmpty()) {
                    val currentQuestion = selectedQuestions.first()
                    _quizUiState.update {
                        it.copy(
                            isQuizStarted = true,
                            quizDifficulty = difficulty,
                            quizType = quizType,
                            questions = selectedQuestions,
                            currentQuestion = currentQuestion,
                            currentQuestionIndex = 0,
                            quizComplete = false,
                            isLoading = false,
                            optionsAndAnswer = currentQuestion.getOptionsAndAnswers().shuffled()
                        )
                    }
                } else {
                    _quizUiState.update {
                        it.copy(isLoading = false)
                    }
                }
            } else {
                _quizUiState.update {
                    it.copy(
                        isQuizStarted = true,
                        quizDifficulty = difficulty,
                        quizType = quizType,
                        isLoading = false
                    )
                }
            }

            quizDatastore.storeDate()
        }
    }

    fun selectAnswer(answer: String) {
        _quizUiState.update {
            it.copy(selectedAnswer = answer)
        }
    }


    fun nextQuestion() {

        _quizUiState.update { currentState ->
            val nextQuestionIndex = currentState.currentQuestionIndex + 1
            if (nextQuestionIndex < currentState.questions.size) {

                val nextQuestion = currentState.questions[nextQuestionIndex]
                    currentState.copy(
                        currentQuestionIndex = nextQuestionIndex,
                        selectedAnswer = "",
                        showCorrectAnswer = false,
                        currentQuestion = nextQuestion,
                        optionsAndAnswer = nextQuestion.getOptionsAndAnswers().shuffled()
                    )

            } else {
                currentState.copy(
                    showCongratulationsScreen = true,
                    selectedAnswer = "",
                    showCorrectAnswer = false
                )
            }
        }
    }

    fun triggerShowCorrectAnswer() {

        viewModelScope.launch {
            updateStreak()
            _quizUiState.update {
                it.copy(
                    showCorrectAnswer = true
                )
            }
            delay(3500L)
                nextQuestion()
        }
    }

    fun restoreLife() {
        viewModelScope.launch {
            _quizUiState.update { currentState ->
                val updatedState = when (currentState.quizType) {
                    QuizType.DRAIN_LAYING -> currentState.copy(
                        drainLayingQuizState = currentState.drainLayingQuizState.copy(lives = 1)
                    )

                    QuizType.PLUMBING -> currentState.copy(
                        plumbingQuizState = currentState.plumbingQuizState.copy(lives = 1)
                    )

                    QuizType.GASFITTING -> currentState.copy(
                        gasFittingQuizState = currentState.gasFittingQuizState.copy(lives = 1)
                    )

                    else -> currentState
                }
                updatedState.copy(isShowingRewardSuccess = true)
            }

            quizDatastore.storeCurrentLives(1, _quizUiState.value.quizType)
        }
    }

    fun dismissRewardSuccess() {
        _quizUiState.update { it.copy(isShowingRewardSuccess = false) }
    }

    fun shouldShowInterstitial(currentIndex: Int, totalQuestions: Int): Boolean {
        val questionNumber = currentIndex + 1
        if (totalQuestions <= 0) return false
        val isEveryTen = questionNumber % 7 == 0 && questionNumber != 0
        val isLastQuestion = questionNumber == totalQuestions
        return isEveryTen && !isLastQuestion
    }


    fun loseLife() {
        if (_quizUiState.value.hasUnlimitedLives()) return
        viewModelScope.launch {
            val currentLives = quizUiState.value.getCurrentQuizTypeLives()
            if (currentLives > 0) {
                val updatedLives = currentLives - 1
                setCurrentLives(updatedLives)
                quizDatastore.storeCurrentLives(updatedLives, quizUiState.value.quizType)
            }
        }
    }

    fun updateSubscriptionTier(tier: SubscriptionTier) {
        viewModelScope.launch {
            quizDatastore.storeSubscriptionTier(tier)
            _quizUiState.update { it.copy(subscriptionTier = tier) }
        }
    }

    fun refreshForDifficultyScreen() {
        viewModelScope.launch {
            refreshStats()
        }
    }
    fun updateStreak() {
        val selected = _quizUiState.value.selectedAnswer
        val correct = _quizUiState.value.currentQuestion?.answer == selected
        val updatedStreak = if (correct) _quizUiState.value.answerStreak + 1 else 0
        _quizUiState.update {
            it.copy(answerStreak = updatedStreak)
        }
        if (!correct) {
            loseLife()
        }
        viewModelScope.launch {
            quizDatastore.storeAnswerStreak(updatedStreak)
        }
    }

    private suspend fun refreshStats() {
        if (quizDatastore.isTodayTheSameDayAsTheStoredDate()) {
            _quizUiState.update {
                it.copy(
                    drainLayingQuizState = it.drainLayingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(QuizType.DRAIN_LAYING)
                    ),
                    plumbingQuizState = it.plumbingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(QuizType.PLUMBING)
                    ),
                    gasFittingQuizState = it.gasFittingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(QuizType.GASFITTING)
                    ),
                    answerStreak = quizDatastore.fetchAnswerStreak(),
                )
            }
        } else {
            quizDatastore.resetLivesAndStreak()
            _quizUiState.update {
                it.copy(
                    drainLayingQuizState = it.drainLayingQuizState.copy(lives = STARTING_LIVES),
                    plumbingQuizState = it.plumbingQuizState.copy(lives = STARTING_LIVES),
                    gasFittingQuizState = it.gasFittingQuizState.copy(lives = STARTING_LIVES),
                    answerStreak = STARTING_ANSWER_STREAK,
                )
            }
        }
    }

    private suspend fun initializeUserStats() {
        _quizUiState.update { it.copy(isLoading = true) }

            refreshStats()
            val lastQuizType = quizDatastore.fetchLastQuizType()
            if (lastQuizType != null) {
                _quizUiState.update {
                    it.copy(quizType = lastQuizType)
                }
            }
        val tier = quizDatastore.fetchSubscriptionTier()
        _quizUiState.update { it.copy(subscriptionTier = tier) }

            _quizUiState.update { it.copy(isLoading = false) }
    }


    private fun setCurrentLives(newValue: Int) {
        when (_quizUiState.value.quizType) {
            QuizType.DRAIN_LAYING -> _quizUiState.update {
                it.copy(drainLayingQuizState = it.drainLayingQuizState.copy(lives = newValue))
            }

            QuizType.PLUMBING -> _quizUiState.update {
                it.copy(plumbingQuizState = it.plumbingQuizState.copy(lives = newValue))
            }

            QuizType.GASFITTING -> _quizUiState.update {
                it.copy(gasFittingQuizState = it.gasFittingQuizState.copy(lives = newValue))
            }

            else -> {}
        }
    }
    fun canShowAd(): Boolean {
        val state = _quizUiState.value
        return shouldShowInterstitial(state.currentQuestionIndex, state.questions.size)
    }
    fun handleRewardEarned() {
        viewModelScope.launch {
            restoreLife()
            _quizUiState.update { it.copy(isShowingRewardSuccess = true) }
        }
    }
}