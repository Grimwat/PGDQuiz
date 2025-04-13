package com.example.pgdquiz.ui

data class Question(
    val id: Int,
    val question: String,
    val answer: Any,
    var options: List<String>,
    var isAnswerCorrect: Boolean = false,
    val multipleAnswers: Boolean = false,
    val imageOptions: Boolean = false
) {
    init {
        if (options.isEmpty()) {
            throw IllegalArgumentException("Options cannot be empty for question: $question")
        }
    }

    val shuffledOptions: List<String> by lazy {
        options.shuffled()
    }

    fun correctAnswers(): List<String> = when (answer) {
        is String -> listOf(answer)
        is List<*> -> answer.filterIsInstance<String>()
        else -> emptyList()
    }

    fun isOptionCorrect(option: String): Boolean = correctAnswers().contains(option)
}