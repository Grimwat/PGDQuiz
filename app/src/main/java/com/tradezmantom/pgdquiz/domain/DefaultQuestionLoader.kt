package com.tradezmantom.pgdquiz.domain

import android.content.Context
import android.util.Log
import com.tradezmantom.pgdquiz.R
import com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader
import com.tradezmantom.pgdquiz.ui.data.Question
import com.tradezmantom.pgdquiz.ui.data.QuestionsResponse
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.google.gson.Gson
import java.io.InputStreamReader

class DefaultQuestionLoader(private val context: Context): QuestionLoader {
    private val gson = Gson()

    override fun loadQuestions(quizType: QuizType): List<Question> {

        val resId = when (quizType) {
            QuizType.DRAIN_LAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            else -> {
                Log.d("QuestionLoader", "Waiting for valid QuizType...")
                return emptyList()
            }
        }

        return try {

            context.resources.openRawResource(resId).use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val response = gson.fromJson(reader, QuestionsResponse::class.java)
                    response?.questions ?: emptyList()
                }
            }
        } catch (e: Exception) {
            Log.e("QuestionLoader", "❌ Error loading questions: ${e.message}")
            emptyList()
        }
    }
}