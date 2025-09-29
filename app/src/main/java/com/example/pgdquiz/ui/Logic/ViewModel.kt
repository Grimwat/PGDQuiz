package com.example.pgdquiz.ui.Logic

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.viewModelScope
import com.example.pgdquiz.ui.Data.QuizState
import com.example.pgdquiz.ui.Data.Question
import com.example.pgdquiz.ui.Data.QuestionsResponse
import com.example.pgdquiz.ui.Data.QuizMode
import com.example.pgdquiz.ui.Data.QuizType
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale


class QuizViewModel : ViewModel() {


    val currentQuestion: State<Question?> = derivedStateOf {
        val quizType = _quizType.value
        val quizState = quizStates[quizType] ?: return@derivedStateOf null
        quizState.questions.getOrNull(_currentQuestionIndex.value)
    }


    fun startQuiz(context: Context, mode: QuizMode, quizType: QuizType) {
        _quizType.value = quizType
        _quizMode.value = mode
        checkAndResetDaily(context)

        if (quizStates[quizType]?.questions.isNullOrEmpty()) {
            loadQuestions(context, mode, quizType)
        }
        _isQuizStarted.value = true
    }


    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        if (quizType == QuizType.DEFAULT) return

        _quizType.value = quizType

        val resId = when (quizType) {
            QuizType.DRAINLAYING -> R.raw.drainsquestions
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

            quizStates[quizType] = QuizState(
                questions = selectedQuestions,
                currentQuestionIndex = 0,
                streakCount = 0,
                lives = 5,
                quizComplete = false,
                selectedAnswers = mutableSetOf()
            )

            _currentQuestionIndex.value = 0
            setCurrentLives(5)
            _quizComplete.value = false
            _selectedAnswers.value = mutableSetOf()

        } catch (e: Exception) {
            Log.e("QuizViewModel", "❌ Error loading questions for $quizType: ${e.message}", e)
        }
    }


    fun selectAnswer(answer: String) {
        _selectedAnswers.value = mutableSetOf(answer)
    }


    fun nextQuestion(context: Context) {
        checkAndResetDaily(context)

        val selected = _selectedAnswers.value
        val correct = currentQuestion.value?.let { listOf(it.answer) } ?: emptyList()
        val type = _quizType.value

        if (selected.isNotEmpty()) {
            if (selected == correct.toSet()) {
                updateStreak(context, type, correct = true)
            } else {
                updateStreak(context, type, correct = false)
                loseLife(context, type)
            }
        }

        if (_currentQuestionIndex.value < (quizStates[type]?.questions?.size ?: 0) - 1) {
            _currentQuestionIndex.value++
        } else {
            _quizComplete.value = true
        }

        _selectedAnswers.value = mutableSetOf()
        _showCorrectAnswer.value = false
    }

    fun triggerShowCorrectAnswer(context: Context) {
        _showCorrectAnswer.value = true
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
                val resetLives = QuizType.values().associateWith { 5 }.toMutableMap()
                val resetStreaks = QuizType.values().associateWith { 0 }.toMutableMap()

                _livesMap.value = resetLives
                _streakMap.value = resetStreaks

                with(prefs.edit()) {
                    putString("last_reset_date", today)
                    QuizType.values().forEach { type ->
                        putInt("${type.name}_lives", 5)
                        putInt("${type.name}_streak", 0)
                    }
                    apply()
                }
            } else {
                val loadedLives = QuizType.values().associateWith { type ->
                    prefs.getInt("${type.name}_lives", 5)
                }.toMutableMap()

                val loadedStreaks = QuizType.values().associateWith { type ->
                    prefs.getInt("${type.name}_streak", 0)
                }.toMutableMap()

                _livesMap.value = loadedLives
                _streakMap.value = loadedStreaks
            }
        }
    }

    fun loseLife(context: Context, quizType: QuizType) {
        val current = _livesMap.value[quizType] ?: 5
        if (current > 0) {
            val updated = current - 1
            _livesMap.value = _livesMap.value.toMutableMap().apply {
                this[quizType] = updated
            }
            val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)
            prefs.edit().putInt("${quizType.name}_lives", updated).apply()
        }
    }

    fun updateStreak(context: Context, quizType: QuizType, correct: Boolean) {
        val current = _streakMap.value[quizType] ?: 0
        val updated = if (correct) current + 1 else 0

        _streakMap.value = _streakMap.value.toMutableMap().apply {
            this[quizType] = updated
        }
        val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)
        prefs.edit().putInt("${quizType.name}_streak", updated).apply()
    }

    val currentLives: Int
        get() = _livesMap.value[_quizType.value] ?: 5

    val currentStreak: Int
        get() = _streakMap.value[_quizType.value] ?: 0

    private val _livesMap = mutableStateOf(
        mutableMapOf(
            QuizType.DRAINLAYING to 5,
            QuizType.PLUMBING to 5,
            QuizType.GASFITTING to 5
        )
    )
    val livesMap: State<Map<QuizType, Int>> = _livesMap

    private val _streakMap = mutableStateOf(
        mutableMapOf(
            QuizType.DRAINLAYING to 0,
            QuizType.PLUMBING to 0,
            QuizType.GASFITTING to 0
        )
    )
    val streakMap: State<Map<QuizType, Int>> = _streakMap

    private fun setCurrentLives(newValue: Int) {
        _livesMap.value = _livesMap.value.toMutableMap().apply {
            this[_quizType.value] = newValue
        }
    }


    private val quizStates: MutableMap<QuizType, QuizState> = mutableMapOf(
        QuizType.DRAINLAYING to QuizState(),
        QuizType.PLUMBING to QuizState(),
        QuizType.GASFITTING to QuizState()
    )

    private val gson = Gson()

    private val _quizType = mutableStateOf(QuizType.DEFAULT)
    val quizType: State<QuizType> = _quizType

    private val _quizMode = mutableStateOf(QuizMode.EASY)
    val quizMode: State<QuizMode> = _quizMode

    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _showCorrectAnswer = mutableStateOf(false)
    val showCorrectAnswer: State<Boolean> = _showCorrectAnswer

    private val _selectedAnswers = mutableStateOf(mutableSetOf<String>())
    val selectedAnswers: State<Set<String>> = _selectedAnswers

    private val _quizComplete = mutableStateOf(false)
    val quizComplete: State<Boolean> = _quizComplete

    private var _isQuizStarted = mutableStateOf(false)
    val isQuizStarted: Boolean get() = _isQuizStarted.value
}