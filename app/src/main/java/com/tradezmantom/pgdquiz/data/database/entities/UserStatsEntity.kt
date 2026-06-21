package com.tradezmantom.pgdquiz.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 0,
    val lastPlayedTimestamp: Long = 0L,
    val currentStreak: Int = 0,
    val lastQuizType: QuizType? = null,
    val livesPerType: Map<QuizType, Int> = emptyMap(),
    val subscriptionTier: SubscriptionTier = SubscriptionTier.FREE
)
