package com.example.pgdquiz.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R
import com.google.gson.Gson
import java.io.InputStreamReader


class QuizViewModel : ViewModel() {
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


    fun loadQuestionsFromRawResource(context: Context) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.drainsquestions)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            Log.d("QuizViewModel", "Raw JSON content: $jsonString")

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)

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
                val finalOptions = if (newOptions.size < 2) listOf(safeAnswer, "Unknown") else newOptions

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
            _currentQuestionIndex.value = 0
        }

        _selectedAnswer.value = null
    }
}
