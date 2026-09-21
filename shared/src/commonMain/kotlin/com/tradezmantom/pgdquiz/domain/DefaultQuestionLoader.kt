package com.tradezmantom.pgdquiz.domain

import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader
import com.tradezmantom.pgdquiz.ui.data.Question
import com.tradezmantom.pgdquiz.ui.data.QuizType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class QuestionDto(
    val id: Int? = null,
    val question: String,
    val answer: String,
    val options: List<String> = emptyList()
)

@Serializable
internal data class QuestionsResponseDto(
    val questions: List<QuestionDto> = emptyList()
)

private val questionsJson = Json { ignoreUnknownKeys = true }

internal fun parseQuestionsJson(json: String, quizType: QuizType): List<Question> {
    val response = questionsJson.decodeFromString<QuestionsResponseDto>(json)
    return response.questions.map { dto ->
        Question(
            id = dto.id,
            question = dto.question,
            answer = dto.answer,
            options = dto.options,
            quizType = quizType
        )
    }
}

internal fun rawQuestionResourceName(quizType: QuizType): String? = when (quizType) {
    QuizType.DRAIN_LAYING -> "drainsquestions"
    QuizType.PLUMBING -> "plumbingquestions"
    QuizType.GASFITTING -> "gasquestions"
    QuizType.DEFAULT -> null
}

expect class DefaultQuestionLoader(platformContext: PlatformContext) : QuestionLoader {
    override suspend fun loadQuestions(quizType: QuizType): List<Question>
}
