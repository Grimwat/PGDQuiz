package com.example.pgdquiz.ui.data

data class QuestionsResponse(
    val questions: List<Question>
)

data class Question(
    val id: Int?,
    val question: String,
    val answer: String,
    val options: List<String>,
) {
    fun isOptionCorrect(option: String): Boolean = option == answer

    fun getOptionsAndAnswers(): List<String> = (options + answer).shuffled()

}