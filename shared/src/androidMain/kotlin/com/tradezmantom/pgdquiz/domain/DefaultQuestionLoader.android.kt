package com.tradezmantom.pgdquiz.domain

import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.ui.data.Question
import com.tradezmantom.pgdquiz.ui.data.QuizType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class DefaultQuestionLoader actual constructor(
    private val platformContext: PlatformContext
) : com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader {

    actual override suspend fun loadQuestions(quizType: QuizType): List<Question> {
        val resourceName = rawQuestionResourceName(quizType) ?: return emptyList()
        return withContext(Dispatchers.IO) {
            try {
                val context = platformContext.context
                val resId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
                if (resId == 0) return@withContext emptyList()
                val json = context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
                parseQuestionsJson(json, quizType)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
