package com.example.pgdquiz.ui

import android.content.Context
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

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }

    private val gson = Gson()

    fun loadQuestionsFromRawResource(context: Context) {
        val inputStream = context.resources.openRawResource(R.raw.drainsquestions)
        val reader = InputStreamReader(inputStream)
        val loadedQuestions = gson.fromJson(
            reader,
            QuestionsResponse::class.java
        ).questions

        reader.close()
        inputStream.close()

        questions = loadedQuestions

        _currentQuestionIndex.value = 0
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