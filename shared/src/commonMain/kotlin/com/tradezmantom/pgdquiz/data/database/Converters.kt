package com.tradezmantom.pgdquiz.data.database

import androidx.room.TypeConverter
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    @TypeConverter
    fun fromLivesMap(map: Map<QuizType, Int>?): String {
        return json.encodeToString(map ?: emptyMap())
    }

    @TypeConverter
    fun toLivesMap(value: String?): Map<QuizType, Int> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromQuizType(quizType: QuizType?): String? {
        return quizType?.name
    }

    @TypeConverter
    fun toQuizType(value: String?): QuizType? {
        return value?.let { QuizType.valueOf(it) }
    }

    @TypeConverter
    fun fromSubscriptionTier(tier: SubscriptionTier?): String? {
        return tier?.name
    }

    @TypeConverter
    fun toSubscriptionTier(value: String?): SubscriptionTier? {
        return value?.let { SubscriptionTier.valueOf(it) }
    }
}
