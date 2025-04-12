package com.example.pgdquiz.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.ui.QuizMode
import com.example.pgdquiz.ui.ui.QuizType
import com.google.gson.Gson
import java.io.InputStreamReader


class QuizViewModel : ViewModel() {
    private var allQuestions: List<Question> = emptyList()
    private var questions: List<Question> = emptyList()
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: MutableState<Int> = _currentQuestionIndex

    val currentQuestion: Question?
        get() = questions.getOrNull(_currentQuestionIndex.value)

    private val _streakCount = mutableStateOf(0)
    val streakCount: MutableState<Int> = _streakCount

    private val _lives = mutableStateOf(3)
    val lives: MutableState<Int> = _lives

    private val _selectedAnswer = mutableStateOf<String?>(null)
    val selectedAnswer: MutableState<String?> = _selectedAnswer

    private val gson = Gson()

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }


    fun loadQuestionsFromRawResource(context: Context, resId: Int) {
        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            Log.d("QuizViewModel", "Raw JSON content: $jsonString")

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)
            allQuestions = parsedResponse.questions.mapNotNull { question -> ... }
            questions = allQuestions
            reader.close()
            inputStream.close()

            if (parsedResponse?.questions.isNullOrEmpty()) {
                Log.e("QuizViewModel", "No questions loaded!")
                return
            }

            questions = parsedResponse.questions.mapNotNull { question ->

                if (question == null || question.answer.isNullOrEmpty()) {
                    Log.e("QuizViewModel", "Skipping invalid question: $question")
                    return@mapNotNull null
                }

                val safeAnswer = question.answer
                val safeOptions = question.options?.filter { it.isNotEmpty() } ?: emptyList()
                val newOptions = (safeOptions + safeAnswer).distinct().shuffled()
                val finalOptions =
                    if (newOptions.size < 2) listOf(safeAnswer, "Unknown") else newOptions

                question.copy(options = finalOptions)

                question.copy(options = finalOptions)
            }

            if (questions.isEmpty()) {
                Log.e("QuizViewModel", "All questions were invalid or missing answers.")
            }

            Log.d("QuizViewModel", "Loaded Questions: $questions")

            _currentQuestionIndex.value = 0
        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error loading questions: ${e.message}")
            e.printStackTrace()
        }
    }

    fun restoreLife() {
        _lives.value = 1
    }

    private val _quizComplete = mutableStateOf(false)
    val quizComplete: MutableState<Boolean> = _quizComplete

    fun nextQuestion() {
        val selected = _selectedAnswer.value
        val correct = currentQuestion?.answer

        if (selected != null && correct != null) {
            if (selected == correct) {
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

        _selectedAnswer.value = null
    }

    fun restartQuiz(
        mode: QuizMode, context: Context) {
        _lives.value = 3
        _streakCount.value = 0
        _selectedAnswer.value = null
        _quizComplete.value = false
        loadQuestions(context, mode)
    }

    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        val resId = when (quizType) {
            QuizType.DRAINLAYING -> R.raw.drainsquestions
//            QuizType.PLUMBING -> R.raw.plumbingquestions
           QuizType.GASFITTING -> R.raw.gasquestions
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