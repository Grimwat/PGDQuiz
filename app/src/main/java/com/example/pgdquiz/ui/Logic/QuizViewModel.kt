package com.example.pgdquiz.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgdquiz.network.QuestionLoader
import com.example.pgdquiz.network.QuizDatastore
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
        val isQuizTypeTheSame = quizType == quizUiState.value.quizType
        _quizUiState.update {
            it.copy(
                isQuizStarted = true,
                quizDifficulty = difficulty,
                quizType = quizType
            )
        }
        quizDatastore.storeDate()
        checkLivesAndStreak()

        if (_quizUiState.value.questions.isEmpty() && isQuizTypeTheSame) {
            loadQuestions()
        }
    }

    fun loadQuestions() {
        if (quizUiState.value.quizType == QuizType.DEFAULT) return

        val allQuestions = questionLoader.loadQuestions(quizUiState.value.quizType)

        val fixedQuestions = allQuestions
            .map { question ->
               println("FUCK ${question.id}")
               println("FUCK ${question}")
                val safeOptions = question.options.filter { it.isNotEmpty() }
                val combined = (safeOptions + question.answer).distinct().shuffled()
                val finalOptions = combined.ifEmpty { listOf("Unknown") }

                question.copy(shuffledOptions = finalOptions)
            }

        val selectedQuestions = fixedQuestions.shuffled().take(
            when (quizUiState.value.quizDifficulty) {
                QuizDifficulty.EASY -> 25
                QuizDifficulty.MEDIUM -> 50
                QuizDifficulty.HARD -> 100
            }
        )

        _quizUiState.update {
            it.copy(
                questions = selectedQuestions,
                currentQuestionIndex = 0, // shouldn't do this when loading questions
                quizComplete = false, // shouldn't do this when loading questions
                currentQuestion = selectedQuestions.first()
            )
        }
    }


    fun selectAnswer(answer: String) {
        _quizUiState.update {
            it.copy(selectedAnswer = answer)
        }
    }


    fun nextQuestion() {
        updateStreak()
        val nextQuestionIndex = _quizUiState.value.currentQuestionIndex + 1
        val thereIsMoreQuestions = nextQuestionIndex < _quizUiState.value.questions.size
        _quizUiState.update {
            if (thereIsMoreQuestions) {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswer = "",
                    showCorrectAnswer = false,
                    currentQuestion = _quizUiState.value.questions[nextQuestionIndex]
                )
            } else {
                it.copy(
                    showCongratulationsScreen = true,
                    selectedAnswer = "",
                    showCorrectAnswer = false
                )
            }
        }
    }

    fun triggerShowCorrectAnswer() {
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
        setCurrentLives(1)
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