package com.example.pgdquiz.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R
import androidx.compose.runtime.State
import com.google.gson.Gson
import java.io.InputStreamReader


class QuizViewModel : ViewModel() {
    private var allQuestions: List<Question> = emptyList()
    private var questions: List<Question> = emptyList()
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: MutableState<Int> = _currentQuestionIndex

    private val _quizType = mutableStateOf(QuizType.DEFAULT)
    val quizType: State<QuizType> = _quizType

    val currentQuestion: Question?
        get() = questions.getOrNull(_currentQuestionIndex.value)

    private val _streakCount = mutableStateOf(0)
    val streakCount: MutableState<Int> = _streakCount

    private val _lives = mutableStateOf(3)
    val lives: MutableState<Int> = _lives

    private val _selectedAnswers = mutableStateOf(mutableSetOf<String>())
    val selectedAnswers: State<Set<String>> get() = _selectedAnswers

    private val _quizComplete = mutableStateOf(false)
    val quizComplete: MutableState<Boolean> = _quizComplete

    private val gson = Gson()

    fun selectAnswer(answer: String) {
        val current = _selectedAnswers.value
        if (currentQuestion?.multipleAnswers == true) {
            if (current.contains(answer)) {
                current.remove(answer)
            } else {
                current.add(answer)
            }
            _selectedAnswers.value = current.toMutableSet()
        } else {
            _selectedAnswers.value = mutableSetOf(answer)
        }
    }

    fun loadQuestionsFromRawResource(context: Context, resId: Int) {
        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            Log.d("QuizViewModel", "Raw JSON content: $jsonString")

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)
            reader.close()
            inputStream.close()

            val filteredQuestions = parsedResponse?.questions?.mapNotNull { question ->
                if (question == null) return@mapNotNull null

                val correctAnswers = when (val ans = question.answer) {
                    is String -> listOf(ans)
                    is List<*> -> ans.filterIsInstance<String>()
                    else -> emptyList()
                }

                if (correctAnswers.isEmpty()) {
                    Log.e("QuizViewModel", "Skipping question with no valid answers: $question")
                    return@mapNotNull null
                }

                val safeOptions = question.options?.filter { it.isNotEmpty() } ?: emptyList()
                val newOptions = (safeOptions + correctAnswers).distinct().shuffled()
                val finalOptions = if (newOptions.size < 2) listOf(correctAnswers.first(), "Unknown") else newOptions

                question.copy(options = finalOptions)
            } ?: emptyList()

            allQuestions = filteredQuestions
            questions = filteredQuestions
            _currentQuestionIndex.value = 0

            if (questions.isEmpty()) {
                Log.e("QuizViewModel", "All questions were invalid or missing answers.")
            } else {
                Log.d("QuizViewModel", "Loaded Questions: $questions")
            }

        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error loading questions: ${e.message}")
            e.printStackTrace()
        }
    }
    fun reset(quizType: QuizType = QuizType.DEFAULT) {
        allQuestions = emptyList()
        questions = emptyList()
        _currentQuestionIndex.value = 0
        _streakCount.value = 0
        _lives.value = 3
        _quizComplete.value = false
        _selectedAnswers.value = mutableSetOf()
        _quizType.value = quizType

    fun restoreLife() {
        _lives.value = 1
    }

    fun nextQuestion() {
        val selected = _selectedAnswers.value
        val correct = currentQuestion?.correctAnswers() ?: emptyList()

        if (selected.isNotEmpty()) {
            if (selected.toSet() == correct.toSet()) {
                _streakCount.value++
            } else {
                _streakCount.value = 0
                if (_lives.value > 0) _lives.value--
            }
        }

        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value++
        } else {
            _quizComplete.value = true
        }

        _selectedAnswers.value = mutableSetOf()
    }

    fun restartQuiz(mode: QuizMode, context: Context, type: QuizType) {
        _quizType.value = type

        val resId = when (type) {
            QuizType.DRAINLAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            QuizType.DEFAULT -> error("QuizType.DEFAULT should not be used here")
        }

        loadQuestionsFromRawResource(context, resId)

        val randomized = when (mode) {
            QuizMode.EASY -> allQuestions.shuffled().take(25)
            QuizMode.MEDIUM -> allQuestions.shuffled().take(50)
            QuizMode.HARD -> {
                val base = allQuestions.shuffled().take(50)
                (base + base.shuffled().take(50)).shuffled()
            }
        }

        questions = randomized
        _currentQuestionIndex.value = 0
        _lives.value = 3
        _streakCount.value = 0
        _quizComplete.value = false
    }
    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        val resId = when (quizType) {
            QuizType.DRAINLAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            QuizType.DEFAULT -> error("QuizType.DEFAULT should not be used here")
        }

        loadQuestionsFromRawResource(context, resId)

        questions = when (mode) {
            QuizMode.EASY -> allQuestions.shuffled().take(25)
            QuizMode.MEDIUM -> allQuestions.shuffled().take(50)
            QuizMode.HARD -> {
                val base = allQuestions.shuffled().take(50)
                (base + base.shuffled().take(50)).shuffled()
            }
        }

        _currentQuestionIndex.value = 0
    }
}