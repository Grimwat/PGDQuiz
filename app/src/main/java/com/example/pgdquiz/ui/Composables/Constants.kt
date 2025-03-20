package com.example.pgdquiz.ui.Composables

import com.example.pgdquiz.ui.Question

object Constants {

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        val que1 = Question(
            1,
            "According to AS/NZS3500 Part 2: Sanitary plumbing and drainage, what is the minimum height an overflow relief gully trap grate is permitted to be installed above unpaved ground level?",
            "50mm",
            "75mm",
            "100mm",
            "125mm",
            "150mm",
            2
        )
        questionsList.add(que1)

        val que2 = Question(
            2,
            "A Section of drain acting as a vent must not be smaller than what size?",
            "50mm",
            "32mm",
            "65mm",
            "80mm",
            "40mm",
            3
        )
        questionsList.add(que2)
        val que3 = Question(
            3,
            "What is the minimum permitted width of a trench excavated for a 150mm drain?",
            "350mm",
            "200mm",
            "300mm",
            "250mm",
            "450mm",
            1
        )
        questionsList.add(que3)
        return questionsList
    }
}