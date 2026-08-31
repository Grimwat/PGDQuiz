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
    val subscriptionTier: SubscriptionTier = SubscriptionTier.FREE
)
fun QuizUiState.isPremium() = subscriptionTier != SubscriptionTier.FREE
fun QuizUiState.hasNoAds() = subscriptionTier == SubscriptionTier.APPRENTICE
        || subscriptionTier == SubscriptionTier.TRADESMAN
fun QuizUiState.hasUnlimitedLives() = subscriptionTier != SubscriptionTier.FREE

fun QuizUiState.getCurrentQuizTypeLives(): Int {
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