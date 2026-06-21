package com.tradezmantom.pgdquiz.ui.data

import com.tradezmantom.pgdquiz.util.STARTING_LIVES

data class IndividualDisciplinesQuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val lives: Int = STARTING_LIVES,
)