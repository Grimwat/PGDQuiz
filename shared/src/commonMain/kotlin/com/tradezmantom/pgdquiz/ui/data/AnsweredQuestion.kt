package com.tradezmantom.pgdquiz.ui.data

// One submitted answer from the current quiz run, kept in order for the post-quiz review.
data class AnsweredQuestion(
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String
) {
    val isCorrect: Boolean get() = selectedAnswer == correctAnswer
}
