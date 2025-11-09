package com.example.pgdquiz.fakes

import com.example.pgdquiz.network.interfaces.QuestionLoader
import com.example.pgdquiz.ui.data.Question
import com.example.pgdquiz.ui.data.QuizType

class FakeQuestionLoader : QuestionLoader {
    override fun loadQuestions(quizType: QuizType): List<Question> {
        // Use a when statement to return the correct list based on the quizType
        return when (quizType) {
            QuizType.PLUMBING -> listOf(
                Question(
                    id = 0,
                    question = "What is the primary purpose of a P-trap?",
                    answer = "To block sewer gases",
                    options = listOf(
                        "To catch fallen jewelry",
                        "To increase water pressure",
                        "To filter water"
                    ),
                    quizType = QuizType.PLUMBING
                ),
                Question(
                    id = 1,
                    question = "What does 'potable water' mean?",
                    answer = "Water that is safe to drink",
                    options = listOf("Hot water", "Wastewater", "Unfiltered water"),
                    quizType = QuizType.PLUMBING
                ),
                Question(
                    id = 2,
                    question = "What does 'grey water' mean?",
                    answer = "Water that is unsafe to drink",
                    options = listOf("Hot water", "Wastewater", "Unfiltered water"),
                    quizType = QuizType.PLUMBING
                )
            )

            QuizType.GASFITTING -> listOf(
                Question(
                    id = 0,
                    question = "What color are gas pipes typically?",
                    answer = "Yellow",
                    options = listOf("Blue", "Red"),
                    quizType = QuizType.GASFITTING
                ),
                Question(
                    id = 1,
                    question = "Atmospheric pressure is?",
                    answer = "301kpa",
                    options = listOf("50kpa", "200kpa"),
                    quizType = QuizType.GASFITTING
                ),
                Question(
                    id = 2,
                    question = "how long do you test for?",
                    answer = "7min",
                    options = listOf("2min", "30min"),
                    quizType = QuizType.GASFITTING
                )
            )
            QuizType.DRAIN_LAYING -> listOf(
                Question(
                    id = 0,
                    question = "What is the primary purpose of fall?",
                    answer = "to let waste flow",
                    options = listOf("to catch fallen jewelry", "to increase water pressure"),
                    quizType = QuizType.DRAIN_LAYING
                ),
                Question(
                    id = 1,
                    question = "What is a gully?",
                    answer = "a source for discharge pipes",
                    options = listOf("A vent", "swimming pool"),
                    quizType = QuizType.DRAIN_LAYING
                ),
                Question(
                    id = 2,
                    question = "What is a septic tank?",
                    answer = "Poo tank",
                    options = listOf("water tank", "swimming pool"),
                    quizType = QuizType.DRAIN_LAYING
                )

            )


            else -> emptyList()
        }
    }
}

