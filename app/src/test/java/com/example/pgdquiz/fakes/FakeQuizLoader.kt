package com.example.pgdquiz.fakes

import com.example.pgdquiz.network.interfaces.QuestionLoader
import com.example.pgdquiz.ui.data.Question
import com.example.pgdquiz.ui.data.QuizType

class FakeQuestionLoader : QuestionLoader {
    override fun loadQuestions(quizType: QuizType): List<Question> {
        return listOf(
            Question(
                id = 0,
                question = "Chris is a what?",
                answer = "Coder",
                options = listOf("wanker", "Hacker", "Goob"),
            )
        )
    }
}
