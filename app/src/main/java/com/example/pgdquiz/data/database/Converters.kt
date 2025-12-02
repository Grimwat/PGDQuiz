package com.example.pgdquiz.data.database

import androidx.room.TypeConverter
import com.example.pgdquiz.ui.data.QuizType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromLivesMap(map: Map<QuizType, Int>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toLivesMap(json: String): Map<QuizType, Int> {
        val type = object : TypeToken<Map<QuizType, Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
    }
}
