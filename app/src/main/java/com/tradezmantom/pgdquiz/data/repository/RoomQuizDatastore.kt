package com.tradezmantom.pgdquiz.data.repository

import com.tradezmantom.pgdquiz.data.database.dao.UserStatsDao
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class RoomQuizDatastore(
    private val userStatsDao: UserStatsDao
) : QuizDatastore {


    override suspend fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return withContext(Dispatchers.IO) {
            val stats = userStatsDao.getOrInitUserStats()
            val storedTime = stats.lastPlayedTimestamp
            if (storedTime == 0L) return@withContext false



            val storedCalendar = Calendar.getInstance().apply { timeInMillis = storedTime }
            val currentCalendar = Calendar.getInstance()
                        storedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        storedCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
        }
    }
    override suspend fun fetchSubscriptionTier(): SubscriptionTier = withContext(Dispatchers.IO) {
        userStatsDao.getOrInitUserStats().subscriptionTier
    }

    override suspend fun storeSubscriptionTier(tier: SubscriptionTier) = withContext(Dispatchers.IO) {
        val currentStats = userStatsDao.getOrInitUserStats()
        userStatsDao.insertOrUpdate(currentStats.copy(subscriptionTier = tier))
    }
    override suspend fun storeDate() = withContext((Dispatchers.IO)){
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(lastPlayedTimestamp = System.currentTimeMillis())
            userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun resetLivesAndStreak() = withContext(Dispatchers.IO) {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(
                currentStreak = 0,
                livesPerType = emptyMap()
            )
            userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun storeAnswerStreak(answerStreak: Int) = withContext(Dispatchers.IO) {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(currentStreak = answerStreak)
            userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun fetchAnswerStreak(): Int = withContext(Dispatchers.IO){
        userStatsDao.getOrInitUserStats().currentStreak
    }

    override suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) = withContext(Dispatchers.IO) {
            val currentStats = userStatsDao.getOrInitUserStats()
            val newMap = currentStats.livesPerType.toMutableMap()
        newMap[quizType] = numberOfLives
        val updatedStats = currentStats.copy(livesPerType = newMap)
        userStatsDao.insertOrUpdate(updatedStats)
    }

    override suspend fun fetchCurrentLives(quizType: QuizType): Int = withContext(Dispatchers.IO) {
        userStatsDao.getOrInitUserStats().livesPerType[quizType]?: STARTING_LIVES
    }
    override suspend fun fetchLastQuizType(): QuizType? = withContext(Dispatchers.IO) {
        userStatsDao.getOrInitUserStats().lastQuizType }
    override suspend fun storeLastQuizType(quizType: QuizType) = withContext(Dispatchers.IO) {
        val currentStats = userStatsDao.getOrInitUserStats()
        val updatedStats = currentStats.copy(lastQuizType = quizType)
        userStatsDao.insertOrUpdate(updatedStats)
    }
}
