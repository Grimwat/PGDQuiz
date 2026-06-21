package com.tradezmantom.pgdquiz.data.database

import androidx.room.TypeConverter
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        private val gson = Gson()
    }

    @TypeConverter
    fun fromLivesMap(map: Map<QuizType, Int>?): String {
        return gson.toJson(map ?: emptyMap<QuizType, Int>())
    }

    @TypeConverter
    fun toLivesMap(json: String?): Map<QuizType, Int> {
        if (json == null) return emptyMap()
        val type = object : TypeToken<Map<QuizType, Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
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