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
        return questionsList
    }
}