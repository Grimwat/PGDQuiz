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
        Log.d("QuizViewModel", "🎯 startQuiz() called with $quizType in $mode")
        _quizType.value = quizType
        _quizMode.value = mode
        checkAndResetLivesDaily(context)
        loadQuestions(context, mode, quizType)
    }

    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        if (quizType == QuizType.DEFAULT) {
            Log.e("QuizViewModel", "❌ Cannot load questions for DEFAULT quiz type")
            return
        }

        _quizType.value = quizType
        Log.d("QuizViewModel", "🟡 Loading questions for $quizType in $mode mode")

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
            Log.d("QuizViewModel", "📦 JSON loaded successfully (length=${jsonString.length})")

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)
            reader.close()
            inputStream.close()

            Log.d("QuizViewModel", "📋 Parsed ${parsedResponse.questions.size} questions from JSON")

            if (parsedResponse.questions.isEmpty()) {
                throw IllegalArgumentException("No questions found in the JSON file for $quizType")
            }

            val fixedQuestions = parsedResponse.questions
                .filterNotNull()
                .map { question ->
                    val safeOptions = question.options.orEmpty().filter { it.isNotEmpty() }
                    val combined = (safeOptions + question.answer).distinct().shuffled()
                    val paddedOptions = when {
                        combined.size >= 4 -> combined.take(4)
                        combined.isNotEmpty() -> combined + List(4 - combined.size) { "Unknown" }
                        else -> List(4) { "Unknown" }
                    }

                    question.copy(
                        options = paddedOptions,
                        shuffledOptions = paddedOptions
                    ).also {
                        Log.d(
                            "QuizViewModel",
                            "🧠 Question: ${it.question.take(30)}... → Options: ${it.options}"
                        )
                    }
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
            _streakCount.value = 0
            setCurrentLives(5)
            _quizComplete.value = false
            _selectedAnswers.value = mutableSetOf()

            Log.d("QuizViewModel", "✅ Loaded ${selectedQuestions.size} questions for $quizType")
            Log.d(
                "QuizViewModel",
                "▶️ First question: ${selectedQuestions.firstOrNull()?.question}"
            )

        } catch (e: Exception) {
            Log.e("QuizViewModel", "❌ Error loading questions for $quizType: ${e.message}", e)
        }
    }

    fun selectAnswer(answer: String) {
        _selectedAnswers.value = mutableSetOf(answer)
    }

    fun nextQuestion() {
        val selected = _selectedAnswers.value
        val correct = currentQuestion.value?.let { listOf(it.answer) } ?: emptyList()
        if (selected.isNotEmpty()) {
            if (selected == correct.toSet()) {
                _streakCount.value++
            } else {
                _streakCount.value = 0
                if (currentLives > 0) setCurrentLives(currentLives - 1)
            }
        }

        if (_currentQuestionIndex.value < (quizStates[_quizType.value]?.questions?.size ?: 0) - 1) {
            _currentQuestionIndex.value++
        } else {
            _quizComplete.value = true
        }

        _selectedAnswers.value = mutableSetOf()
        _showCorrectAnswer.value = false
    }
    fun checkAndResetLivesDaily(context: Context) {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("lives_prefs", Context.MODE_PRIVATE)
            val lastResetDate = prefs.getString("last_reset_date", "")
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = sdf.format(Date())

            if (lastResetDate != today) {
                val resetMap = QuizType.values().associateWith { 5 }.toMutableMap()
                _livesMap.value = resetMap

                prefs.edit().putString("last_reset_date", today).apply()
            }
        }
    }

    fun restoreLife() {
        if (currentLives <= 0) {
            setCurrentLives(1)
            _quizComplete.value = false
        }
    }

    fun reset(quizType: QuizType = QuizType.DEFAULT) {
        _quizType.value = quizType
        _currentQuestionIndex.value = 0
        _streakCount.value = 0
        setCurrentLives(5)
        _quizComplete.value = false
        _selectedAnswers.value = mutableSetOf()

        if (quizType != QuizType.DEFAULT) {
            quizStates[quizType] = QuizState()
        }
    }

    fun restartQuiz(mode: QuizMode, context: Context, quizType: QuizType) {
        reset(quizType)
        loadQuestions(context, mode, quizType)
    }

    fun triggerShowCorrectAnswer() {
        _showCorrectAnswer.value = true
        viewModelScope.launch {
            delay(2000L)
            nextQuestion()
        }
    }

    private val _livesMap = mutableStateOf(
        mutableMapOf(
            QuizType.DRAINLAYING to 5,
            QuizType.PLUMBING to 5,
            QuizType.GASFITTING to 5
        )
    )
    val livesMap: State<Map<QuizType, Int>> = _livesMap

    val currentLives: Int
        get() = _livesMap.value[_quizType.value] ?: 5

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

    private val _streakCount = mutableStateOf(0)
    val streakCount: State<Int> = _streakCount

    private val _showCorrectAnswer = mutableStateOf(false)
    val showCorrectAnswer: State<Boolean> = _showCorrectAnswer

    private val _selectedAnswers = mutableStateOf(mutableSetOf<String>())
    val selectedAnswers: State<Set<String>> = _selectedAnswers

    private val _quizComplete = mutableStateOf(false)
    val quizComplete: State<Boolean> = _quizComplete
}