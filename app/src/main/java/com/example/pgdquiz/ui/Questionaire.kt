package com.example.pgdquiz.ui

data class Question(
    val id: Int,
    val question: String,
    val answer: String? = null,
    var options: List<String>,
    var isAnswerCorrect: Boolean,
) {
    val shuffledOptions: List<String> = (options + answer!!).shuffled()
    val correctAnswerIndex: Int = answers().indexOf(answer)
}

fun Question.answers(): List<String>{
    return options + answer!!
}

data class QuestionsResponse(
    val questions: List<Question>
)