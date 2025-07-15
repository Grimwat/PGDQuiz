package com.example.pgdquiz.ui.Data

data class QuizState(
    var questions: List<Question> = emptyList(),
    var currentQuestionIndex: Int = 0,
    var streakCount: Int = 0,
    var lives: Int = 3,
    var quizComplete: Boolean = false,
    var selectedAnswers: MutableSet<String> = mutableSetOf()
)