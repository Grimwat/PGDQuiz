package com.tradezmantom.pgdquiz.domain.interfaces

import com.tradezmantom.pgdquiz.ui.data.Question
import com.tradezmantom.pgdquiz.ui.data.QuizType

interface QuestionLoader {

    fun loadQuestions(quizType: QuizType): List<Question>
}