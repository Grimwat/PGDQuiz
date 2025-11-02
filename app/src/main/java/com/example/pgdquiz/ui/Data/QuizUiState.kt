package com.example.pgdquiz.ui.data

data class QuizUiState(
    val drainLayingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val plumbingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val gasFittingQuizState: IndividualDisciplinesQuizState = IndividualDisciplinesQuizState(),
    val isQuizStarted: Boolean = false,
    val quizComplete: Boolean = false,
    val selectedAnswer: String = "",
    val showCorrectAnswer: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val quizDifficulty: QuizDifficulty = QuizDifficulty.EASY,
    val quizType: QuizType = QuizType.DEFAULT,
    val currentQuestion: Question? = null,
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val showCongratulationsScreen: Boolean = false,
    val answerStreak: Int = 0,
    val optionsAndAnswer: List<String> = emptyList()
)

fun QuizUiState.getCurrentQuizTypeLives(): Int {
    return when (quizType) {
        QuizType.DRAIN_LAYING -> drainLayingQuizState.lives
        QuizType.PLUMBING -> plumbingQuizState.lives
        QuizType.GASFITTING -> gasFittingQuizState.lives
        else -> 0
    }
}
