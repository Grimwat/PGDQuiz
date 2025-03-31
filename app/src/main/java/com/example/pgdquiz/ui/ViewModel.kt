package com.example.pgdquiz.ui

import android.provider.SyncStateContract.Constants
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.ui.ui.DrainQuestions

class QuizViewModel : ViewModel() {
    private val questions = DrainQuestions.getQuestions() // Fixed reference
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: MutableState<Int> = _currentQuestionIndex
    val currentQuestion: Question
        get() = questions[_currentQuestionIndex.value]

    fun nextQuestion() {
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value++
        } else {
            _currentQuestionIndex.value = 0
        }
    }
}