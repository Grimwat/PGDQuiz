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
    val streakCount: State<Int> = _streakCount

    private val _lives = mutableStateOf(3)
    val lives: State<Int> = _lives

    fun checkAnswer(selectedAnswer: String) {
        if (selectedAnswer == currentQuestion.correctAnswer){
            _streakCount.value++
        } else {
            _streakCount.value = 0
            _lives.value--
        }
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value++
        } else {
            _currentQuestionIndex.value = 0
        }
    }
}