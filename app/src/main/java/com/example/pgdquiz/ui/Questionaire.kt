package com.example.pgdquiz.ui

data class QuestionsResponse(
    val questions: List<Question>
)

data class Question(
    val id: Int,
    val question: String,
    val answer: Any,
    var options: List<String> = emptyList(),
    var isAnswerCorrect: Boolean = false,
    var shuffledOptions: List<String>? = null
) {
    init {
        if ((options.isEmpty() || options.all { it.isBlank() }) && correctAnswers().isEmpty()) {
            throw IllegalArgumentException("Options cannot be empty or invalid for question: $question")
        }
    }

    fun correctAnswers(): List<String> = when (answer) {
        is String -> listOf(answer)
        is List<*> -> answer.filterIsInstance<String>()
        else -> emptyList()
    }

    fun isOptionCorrect(option: String): Boolean = correctAnswers().contains(option)
}