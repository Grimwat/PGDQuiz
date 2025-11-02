package com.example.pgdquiz.network.interfaces

import com.example.pgdquiz.ui.data.Question
import com.example.pgdquiz.ui.data.QuizType

interface QuestionLoader {

    fun loadQuestions(quizType: QuizType): List<Question>
}