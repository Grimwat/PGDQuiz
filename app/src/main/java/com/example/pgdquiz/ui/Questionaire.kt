package com.example.pgdquiz.ui

data class Question(
    val id: Int,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
) {
    val shuffledOptions: List<String> = (incorrectAnswers + correctAnswer).shuffled()
    val correctAnswerIndex: Int = shuffledOptions.indexOf(correctAnswer)
}