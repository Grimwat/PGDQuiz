package com.example.pgdquiz.data.repository

import com.example.pgdquiz.data.database.dao.UserStatsDao
import com.example.pgdquiz.domain.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar

class RoomQuizDatastore(
    private val userStatsDao: UserStatsDao
) : QuizDatastore {


    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return runBlocking(Dispatchers.IO) {
            val stats = userStatsDao.getOrInitUserStats()
            val storedTime = stats.lastPlayedTimestamp
            if (storedTime == 0L) {
                return@runBlocking false
            }


            val storedCalendar = Calendar.getInstance().apply { timeInMillis = storedTime }
            val currentCalendar = Calendar.getInstance()
                        storedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        storedCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
        }
    }

    override fun storeDate() = runBlocking((Dispatchers.IO)){
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(lastPlayedTimestamp = System.currentTimeMillis())
            userStatsDao.insertOrUpdate(updatedStats)
    }

    override fun resetLivesAndStreak() = runBlocking(Dispatchers.IO) {
            val currentStats = userStatsDao.getOrInitUserStats()
            val updatedStats = currentStats.copy(
                currentStreak = 0,
                livesPerType = emptyMap()
            )
            userStatsDao.insertOrUpdate(updatedStats)
    }

    override fun storeAnswerStreak(answerStreak: Int) = runBlocking(Dispatchers.IO) {
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

    override fun fetchCurrentLives(quizType: QuizType): Int = runBlocking(Dispatchers.IO) {
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
