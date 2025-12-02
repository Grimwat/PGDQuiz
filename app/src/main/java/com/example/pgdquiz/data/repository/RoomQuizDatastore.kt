package com.example.pgdquiz.data.repository

import com.example.pgdquiz.data.database.dao.UserStatsDao
import com.example.pgdquiz.domain.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class RoomQuizDatastore(
    private val userStatsDao: UserStatsDao
) : QuizDatastore {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {

        val stats = runBlocking { userStatsDao.getOrInitUserStats() }

        val storedTime = stats.lastPlayedTimestamp
        if (storedTime == 0L) return false

        val storedCalendar = Calendar.getInstance().apply { timeInMillis = storedTime }
        val currentCalendar = Calendar.getInstance()

        return storedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                storedCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
    }

    override fun storeDate() {
        scope.launch {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(lastPlayedTimestamp = System.currentTimeMillis())
            userStatsDao.insertOrUpdate(updatedStats)
        }
    }

    override fun resetLivesAndStreak() {
        scope.launch {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(
                currentStreak = 0,
                livesPerType = emptyMap()
            )
            userStatsDao.insertOrUpdate(updatedStats)
        }
    }

    override fun storeAnswerStreak(answerStreak: Int) {
        scope.launch {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(currentStreak = answerStreak)
            userStatsDao.insertOrUpdate(updatedStats)
        }
    }

    override fun fetchAnswerStreak(): Int {
        val stats = runBlocking { userStatsDao.getOrInitUserStats() }
        return stats.currentStreak
    }

    override fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) {
        scope.launch {
            val currentStats = userStatsDao.getOrInitUserStats()

            val newMap = currentStats.livesPerType.toMutableMap()
            newMap[quizType] = numberOfLives

            val updatedStats = currentStats.copy(livesPerType = newMap)
            userStatsDao.insertOrUpdate(updatedStats)
        }
    }

    override fun fetchCurrentLives(quizType: QuizType): Int {
        val stats = runBlocking { userStatsDao.getOrInitUserStats() }
        return stats.livesPerType[quizType] ?: 3
    }
}
