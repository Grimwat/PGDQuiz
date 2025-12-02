package com.example.pgdquiz.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pgdquiz.ui.data.QuizType

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 0,
    val lastPlayedTimestamp: Long = 0L,
    val currentStreak: Int = 0,
    val livesPerType: Map<QuizType, Int> = emptyMap()
)
