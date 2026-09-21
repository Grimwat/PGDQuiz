package com.tradezmantom.pgdquiz.data.repository

import com.tradezmantom.pgdquiz.data.database.dao.UserStatsDao
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultQuizDatastore(
    private val userStatsDao: UserStatsDao
) : QuizDatastore {

    override suspend fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return withContext(Dispatchers.Default) {
            val stats = userStatsDao.getOrInitUserStats()
            val storedTime = stats.lastPlayedTimestamp
            if (storedTime == 0L) return@withContext false

            val timeZone = TimeZone.currentSystemDefault()
            val storedDate = Instant.fromEpochMilliseconds(storedTime).toLocalDateTime(timeZone).date
            val currentDate = Clock.System.now().toLocalDateTime(timeZone).date
            storedDate == currentDate
        }
    }

    override suspend fun fetchSubscriptionTier(): SubscriptionTier = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().subscriptionTier
    }

    override suspend fun storeSubscriptionTier(tier: SubscriptionTier) = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        userStatsDao.insertOrUpdate(currentStats.copy(subscriptionTier = tier))
    }

    override suspend fun storeDate() = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val updatedStats = currentStats.copy(lastPlayedTimestamp = Clock.System.now().toEpochMilliseconds())
        userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun resetLivesAndStreak() = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val updatedStats = currentStats.copy(
            currentStreak = 0,
            livesPerType = emptyMap()
        )
        userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun storeAnswerStreak(answerStreak: Int) = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val updatedStats = currentStats.copy(currentStreak = answerStreak)
        userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun fetchAnswerStreak(): Int = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().currentStreak
    }

    override suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val newMap = currentStats.livesPerType.toMutableMap()
        newMap[quizType] = numberOfLives
        val updatedStats = currentStats.copy(livesPerType = newMap)
        userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun fetchCurrentLives(quizType: QuizType): Int = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().livesPerType[quizType] ?: STARTING_LIVES
    }

    override suspend fun fetchLastQuizType(): QuizType? = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().lastQuizType
    }

    override suspend fun storeLastQuizType(quizType: QuizType) = withContext(Dispatchers.Default) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val updatedStats = currentStats.copy(lastQuizType = quizType)
        userStatsDao.insertOrUpdate(updatedStats)
    }
}
