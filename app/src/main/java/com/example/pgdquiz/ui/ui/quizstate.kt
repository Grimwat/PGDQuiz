package com.example.pgdquiz.ui.ui

import com.example.pgdquiz.ui.Question

data class QuizState(
    var questions: List<Question> = emptyList(),
    var currentQuestionIndex: Int = 0,
    var streakCount: Int = 0,
    var lives: Int = 3,
    var quizComplete: Boolean = false,
    var selectedAnswers: MutableSet<String> = mutableSetOf()
)