package com.example.pgdquiz.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgdquiz.network.interfaces.QuestionLoader
import com.example.pgdquiz.network.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizDifficulty
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.data.QuizUiState
import com.example.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.example.pgdquiz.util.STARTING_ANSWER_STREAK
import com.example.pgdquiz.util.STARTING_LIVES
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


    fun startQuiz(difficulty: QuizDifficulty, quizType: QuizType) {

        val currentState = quizUiState.value
        val shouldLoadNewQuestions =
            currentState.questions.isEmpty() || quizType != currentState.quizType

        _quizUiState.update {
            it.copy(
                isQuizStarted = true,
                quizDifficulty = difficulty,
                quizType = quizType
            )
        }
        quizDatastore.storeDate()
        checkLivesAndStreak()

        if (shouldLoadNewQuestions) {
            loadQuestions(quizType)
        }
    }


    private fun loadQuestions(quizType: QuizType) {

        _quizUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(50)


            val allQuestions = questionLoader.loadQuestions(quizType)


            val selectedQuestions = allQuestions.shuffled().take(
                when (quizUiState.value.quizDifficulty) {
                    QuizDifficulty.EASY -> 25
                    QuizDifficulty.MEDIUM -> 50
                    QuizDifficulty.HARD -> 100
                }
            )
            if (selectedQuestions.isNotEmpty()) {
                val currentQuestion = selectedQuestions.first()
                _quizUiState.update {
                    it.copy(
                        questions = selectedQuestions,
                        currentQuestion = currentQuestion,
                        currentQuestionIndex = 0,
                        quizComplete = false,
                        isLoading = false,
                        optionsAndAnswer = currentQuestion.getOptionsAndAnswers().shuffled(),


                        )
                }
            } else {
                println("Error: No questions loaded for ${quizUiState.value.quizType}")
                _quizUiState.update {
                    it.copy(
                        isLoading = false

                    )
                }
            }
        }
    }


    fun selectAnswer(answer: String) {
        _quizUiState.update {
            it.copy(selectedAnswer = answer)
        }
    }


    fun nextQuestion() {

        _quizUiState.update { currentState ->
            val nextQuestionIndex = _quizUiState.value.currentQuestionIndex + 1
            val thereIsMoreQuestions = nextQuestionIndex < _quizUiState.value.questions.size
            if (thereIsMoreQuestions) {
                val nextQuestion = _quizUiState.value.questions[nextQuestionIndex]
                currentState.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswer = "",
                    showCorrectAnswer = false,
                    currentQuestion = nextQuestion

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
        updateStreak()
        _quizUiState.update {
            it.copy(
                showCorrectAnswer = true
            )
        }
        viewModelScope.launch {
            delay(2000L)
            nextQuestion()
        }
    }

    fun checkLivesAndStreak() {
        if (quizDatastore.isTodayTheSameDayAsTheStoredDate()) {
            _quizUiState.update {
                it.copy(
                    drainLayingQuizState = it.drainLayingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(
                            QuizType.DRAIN_LAYING
                        )
                    ),
                    plumbingQuizState = it.plumbingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(
                            QuizType.PLUMBING
                        )
                    ),
                    gasFittingQuizState = it.gasFittingQuizState.copy(
                        lives = quizDatastore.fetchCurrentLives(
                            QuizType.GASFITTING
                        )
                    ),
                    answerStreak = quizDatastore.fetchAnswerStreak(),
                )
            }
        } else {
            _quizUiState.update {
                it.copy(
                    drainLayingQuizState = it.drainLayingQuizState.copy(lives = STARTING_LIVES),
                    plumbingQuizState = it.plumbingQuizState.copy(lives = STARTING_LIVES),
                    gasFittingQuizState = it.gasFittingQuizState.copy(lives = STARTING_LIVES),
                    answerStreak = STARTING_ANSWER_STREAK,
                )
            }
            quizDatastore.resetLivesAndStreak()
        }
    }

    fun restoreLife() {
        val currentLives = quizUiState.value.getCurrentQuizTypeLives()
        val updatedLives = currentLives + 1
        setCurrentLives(updatedLives)
        quizDatastore.storeCurrentLives(updatedLives, quizUiState.value.quizType)
    }


    fun loseLife() {
        val currentLives = quizUiState.value.getCurrentQuizTypeLives()
        if (currentLives > 0) {
            val updatedLives = currentLives - 1
            setCurrentLives(updatedLives)
            quizDatastore.storeCurrentLives(updatedLives, quizUiState.value.quizType)
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
        quizDatastore.storeAnswerStreak(updatedStreak)
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

}