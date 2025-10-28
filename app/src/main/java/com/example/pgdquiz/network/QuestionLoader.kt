package com.example.pgdquiz.network

import android.content.Context
import android.util.Log
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.data.Question
import com.example.pgdquiz.ui.data.QuestionsResponse
import com.example.pgdquiz.ui.data.QuizType
import com.google.gson.Gson
import java.io.InputStreamReader

class QuestionLoader(private val context: Context) {
    private val gson = Gson()

    fun loadQuestions(quizType: QuizType): List<Question> {
        val resId = when (quizType) {
            QuizType.DRAIN_LAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            else -> error("Invalid QuizType")
        }

        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()
            reader.close()
            inputStream.close()

            return gson.fromJson(jsonString, QuestionsResponse::class.java).questions
        } catch (e: Exception) {
            Log.e("QuizViewModel", "❌ Error loading questions for $quizType: ${e.message}", e)
            return emptyList()
        }
    }
}