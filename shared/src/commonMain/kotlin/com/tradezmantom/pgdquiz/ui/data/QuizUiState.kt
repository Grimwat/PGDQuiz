package com.tradezmantom.pgdquiz.ui.data

data class QuizUiState(
    val drainLayingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val plumbingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val gasFittingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val isQuizStarted: Boolean = false,
    val quizComplete: Boolean = false,
    val selectedAnswer: String = "",
    val showCorrectAnswer: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val isShowingRewardSuccess: Boolean = false,
    val quizDifficulty: QuizDifficulty = QuizDifficulty.EASY,
    val quizType: QuizType = QuizType.DEFAULT,
    val currentQuestion: Question? = null,
    val isLoading: Boolean = true,
    val questions: List<Question> = emptyList(),
    val showCongratulationsScreen: Boolean = false,
    val answerStreak: Int = 0,
    val bestStreak: Int = 0,
    val correctAnswersCount: Int = 0,
    val optionsAndAnswer: List<String> = emptyList(),
    val subscriptionTier: SubscriptionTier = SubscriptionTier.FREE,
    val showWelcomeOverlay: Boolean = false,
    val answeredQuestions: List<AnsweredQuestion> = emptyList(),
    val questionBankSizes: Map<QuizType, Int> = emptyMap()
)

/** How many questions a run of [difficulty] for [quizType] really has (capped by the bank). */
fun QuizUiState.questionCountFor(quizType: QuizType, difficulty: QuizDifficulty): Int {
    val bankSize = questionBankSizes[quizType] ?: return difficulty.questionCount
    return minOf(difficulty.questionCount, bankSize)
}
fun QuizUiState.isPremium() = subscriptionTier != SubscriptionTier.FREE
fun QuizUiState.hasNoAds() = subscriptionTier == SubscriptionTier.APPRENTICE
        || subscriptionTier == SubscriptionTier.TRADESMAN
fun QuizUiState.hasUnlimitedLives() = subscriptionTier != SubscriptionTier.FREE

fun QuizUiState.getCurrentQuizTypeLives(): Int = livesFor(quizType)

fun QuizUiState.livesFor(quizType: QuizType): Int {
    return when (quizType) {
        QuizType.DRAIN_LAYING -> drainLayingQuizState.lives
        QuizType.PLUMBING -> plumbingQuizState.lives
        QuizType.GASFITTING -> gasFittingQuizState.lives
        else -> 0
    }
}

fun QuizUiState.getCurrentQuestions(): List<Question> {
    return questions
}
