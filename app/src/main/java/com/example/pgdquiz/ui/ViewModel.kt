package com.example.pgdquiz.ui
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.ui.ui.DrainQuestions


class QuizViewModel : ViewModel() {
    private val questions = DrainQuestions.getQuestions()
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: MutableState<Int> = _currentQuestionIndex

    val currentQuestion: Question
        get() = questions[_currentQuestionIndex.value]

    private val _streakCount = mutableStateOf(0)
    val streakCount: MutableState<Int> = _streakCount

    private val _lives = mutableStateOf(3)
    val lives: MutableState<Int> = _lives

    private val _selectedAnswer = mutableStateOf<String?>(null)
    val selectedAnswer: MutableState<String?> = _selectedAnswer

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }

    fun nextQuestion() {
        if (_selectedAnswer.value != null) {
            if (_selectedAnswer.value == currentQuestion.correctAnswer) {
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