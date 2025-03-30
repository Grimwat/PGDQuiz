package com.example.pgdquiz.ui

data class Question (
    val id: Int,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
){
    val shuffledOptions: List<String>
    val incorrectAnswerIndex: Int

    init {
        val allOptions = incorrectAnswers + correctAnswer
        shuffledOptions = allOptions.shuffled()
        incorrectAnswerIndex = shuffledOptions.indexOf(correctAnswer)
    }
}
