package com.tradezmantom.pgdquiz.domain

import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.ui.data.Question
import com.tradezmantom.pgdquiz.ui.data.QuizType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual class DefaultQuestionLoader actual constructor(
    platformContext: PlatformContext
) : com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader {

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun loadQuestions(quizType: QuizType): List<Question> {
        val resourceName = rawQuestionResourceName(quizType) ?: return emptyList()
        val path = NSBundle.mainBundle.pathForResource(resourceName, ofType = "json") ?: return emptyList()
        return try {
            val json = NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null) as String
            parseQuestionsJson(json, quizType)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
