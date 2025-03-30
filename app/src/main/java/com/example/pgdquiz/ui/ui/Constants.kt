package com.example.pgdquiz.ui.ui

import com.example.pgdquiz.ui.Question

object Constants {
    fun getQuestions(): List<Question> {
        return listOf(

            Question(
                id = 1,
                question = "According to AS/NZS3500 Part 2: Sanitary plumbing and drainage, what is the minimum height an overflow relief gully trap grate is permitted to be installed above unpaved ground level?",
                correctAnswer = "75mm",
                incorrectAnswers = listOf("50mm", "100mm", "125mm", "150mm")
            ),
            Question(
                id = 2,
                question = "A section of drain acting as a vent must not be smaller than what size?",
                correctAnswer = "65mm",
                incorrectAnswers = listOf("50mm", "32mm", "80mm", "40mm")
            ),
            Question(
                id = 3,
                question = "What is the minimum permitted width of a trench excavated for a 150mm drain?",
                correctAnswer = "350mm",
                incorrectAnswers = listOf("300mm", "200mm", "250mm", "450mm")
            )
        )

    }
}
}