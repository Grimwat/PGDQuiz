package com.example.pgdquiz.ui

import android.provider.SyncStateContract.Constants
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel (){
    private val questions = Constants.getQuestions()
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex
    val currentQuestion: Question
        get() = questions [_currentQuestionIndex.value]

    fun nextQuestion(){
        if (_currentQuestionIndex.value<questions.size -1) {
            _currentQuestionIndex.value++
        }
    }
}