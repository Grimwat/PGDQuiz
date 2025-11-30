package com.example.pgdquiz.ui.data

data class IndividualDisciplinesQuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val lives: Int = 5,
)