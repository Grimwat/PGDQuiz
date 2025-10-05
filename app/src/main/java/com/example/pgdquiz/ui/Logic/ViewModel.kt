package com.example.pgdquiz.ui.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.data.QuestionsResponse
import com.example.pgdquiz.ui.data.QuizMode
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.data.QuizUiState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizViewModel : ViewModel() {

    private val _quizUiState = MutableStateFlow(QuizUiState())
    val quizUiState: StateFlow<QuizUiState> = _quizUiState.asStateFlow()

    fun startQuiz(context: Context, mode: QuizMode, quizType: QuizType) {
        _quizUiState.update {
            it.copy(
                isQuizStarted = true,
                quizMode = mode,
                quizType = quizType
            )
        }
        checkAndResetDaily(context)

        if (_quizUiState.value.questions.isEmpty()) {
            loadQuestions(context, mode, quizType)
        }
    }


    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        if (quizType == QuizType.DEFAULT) return

        val resId = when (quizType) {
            QuizType.DRAIN_LAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            else -> error("Invalid QuizType")
        }

        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()
            reader.close()
            inputStream.close()

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)

            val fixedQuestions = parsedResponse.questions
                .filterNotNull()
                .map { question ->
                    val safeOptions = question.options.orEmpty().filter { it.isNotEmpty() }
                    val combined = (safeOptions + question.answer).distinct().shuffled()
                    val finalOptions = combined.ifEmpty { listOf("Unknown") }

                    question.copy(shuffledOptions = finalOptions)
                }

            val selectedQuestions = fixedQuestions.shuffled().take(
                when (mode) {
                    QuizMode.EASY -> 25
                    QuizMode.MEDIUM -> 50
                    QuizMode.HARD -> 100
                }
            )

            _quizUiState.update {
                it.copy(
                    questions = selectedQuestions,
                    quizType = quizType,
                    currentQuestionIndex = 0,
                    quizComplete = false,

                    )
            }
        } catch (e: Exception) {
            Log.e("QuizViewModel", "❌ Error loading questions for $quizType: ${e.message}", e)
        }
    }


    fun selectAnswer(answer: String) {
        when (_quizUiState.value.quizType) {
            QuizType.DRAIN_LAYING -> _quizUiState.update {
                it.copy(drainLayingQuizState = it.drainLayingQuizState.copy(selectedAnswer = answer))
            }

            QuizType.PLUMBING -> _quizUiState.update {
                it.copy(plumbingQuizState = it.plumbingQuizState.copy(selectedAnswer = answer))
            }

            QuizType.GASFITTING -> _quizUiState.update {
                it.copy(gasFittingQuizState = it.gasFittingQuizState.copy(selectedAnswer = answer))
            }

            else -> {}
        }
    }


    fun nextQuestion(context: Context) {
        checkAndResetDaily(context)

        val selected = _quizUiState.value.selectedAnswer
        val correct = _quizUiState.value.currentQuestion?.answer == selected
        val type = _quizUiState.value.quizType

        if (correct) {
            updateStreak(context, type, correct = true)
        } else {
            updateStreak(context, type, correct = false)
            loseLife(context, type)
        }

        val nextQuestionIndex = _quizUiState.value.currentQuestionIndex + 1
        _quizUiState.update {
            if (nextQuestionIndex < _quizUiState.value.questions.size) {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswer = "",
                    showCorrectAnswer = false
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

    fun triggerShowCorrectAnswer(context: Context) {
        _quizUiState.update {
            it.copy(
                showCorrectAnswer = true
            )
        }
        viewModelScope.launch {
            delay(2000L)
            nextQuestion(context)
        }
    }


    fun checkAndResetDaily(context: Context) {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)
            val lastResetDate = prefs.getString("last_reset_date", "")
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = sdf.format(Date())

            if (lastResetDate != today) {

                resetDailyState()

                with(prefs.edit()) {
                    putString("last_reset_date", today)
                    QuizType.entries.forEach { type ->
                        putInt("${type.name}_lives", 5)
                    }
                    putInt("answerStreak", 0)
                    apply()
                }
            } else {
                val loadedLives = QuizType.entries.associateWith { type ->
                    prefs.getInt("${type.name}_lives", 5)
                }.toMutableMap()

                val loadedStreaks = prefs.getInt("answerStreak", 0)

                _quizUiState.update {
                    it.copy(
                        drainLayingQuizState = it.drainLayingQuizState.copy(
                            lives = loadedLives[QuizType.DRAIN_LAYING]
                                ?: 5
                        ),
                        plumbingQuizState = it.plumbingQuizState.copy(
                            lives = loadedLives[QuizType.PLUMBING]
                                ?: 5
                        ),
                        gasFittingQuizState = it.gasFittingQuizState.copy(
                            lives = loadedLives[QuizType.GASFITTING]
                                ?: 5
                        ),
                        answerStreak = loadedStreaks,
                    )
                }

            }
        }
    }

    fun resetDailyState() {
        _quizUiState.update {
            it.copy(
                drainLayingQuizState = it.drainLayingQuizState.copy(lives = 5),
                plumbingQuizState = it.plumbingQuizState.copy(lives = 5),
                gasFittingQuizState = it.gasFittingQuizState.copy(lives = 5),
                answerStreak = 0,
            )
        }
    }

    fun restoreLife() {
//        val current = _livesMap.value[quizType] ?: 0
//        if (current == 0) {
//            _livesMap.value = _livesMap.value.toMutableMap().apply {
//                this[quizType] = 1
//            }
//            _quizComplete.value = false
//        }
    }

    fun loseLife(context: Context, quizType: QuizType) {
        val current = when (_quizUiState.value.quizType) {
            QuizType.DRAIN_LAYING -> _quizUiState.value.drainLayingQuizState.lives
            QuizType.PLUMBING -> _quizUiState.value.plumbingQuizState.lives
            QuizType.GASFITTING -> _quizUiState.value.gasFittingQuizState.lives
            else -> 0
        }
        if (current > 0) {
            val updated = current - 1
            setCurrentLives(updated)
            val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)
            prefs.edit().putInt("${quizType.name}_lives", updated).apply()
        }
    }

    fun updateStreak(context: Context, quizType: QuizType, correct: Boolean) {
        val updated = if (correct) _quizUiState.value.answerStreak + 1 else 0
        val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)
        prefs.edit().putInt("answerStreak", updated).apply()
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

    private val gson = Gson()
}