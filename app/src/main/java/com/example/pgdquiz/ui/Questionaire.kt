package com.example.pgdquiz.ui

data class Question(
    val id: Int,
    val question: String,
    val answer: String,
    var options: List<String>,
    var isAnswerCorrect: Boolean = false,
) {
    init {
        if (options.isEmpty()) {
            throw IllegalArgumentException("Options cannot be empty for question: $question")
        }
    }

    val shuffledOptions: List<String> by lazy {
        options.shuffled()
    }

    val correctAnswerIndex: Int by lazy {
        shuffledOptions.indexOf(answer)
    }
}

fun Question.answers(): List<String>{
    return options + answer
}

data class QuestionsResponse(
    val questions: List<Question>
)