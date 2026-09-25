package com.tradezmantom.pgdquiz.data.repository

import com.tradezmantom.pgdquiz.data.database.dao.UserStatsDao
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import com.tradezmantom.pgdquiz.data.database.entities.UserStatsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultQuizDatastore(
    private val userStatsDao: UserStatsDao
) : QuizDatastore {

    // Every write is a read-modify-write of the single stats row. Unserialised, two concurrent
    // writes (e.g. a wrong answer storing both streak and lives) could each read the old row and
    // the later write would silently undo the earlier one, which brought lost lives back on restart.
    private val writeLock = Mutex()

    private suspend fun updateStats(transform: (UserStatsEntity) -> UserStatsEntity) =
        withContext(Dispatchers.Default) {
            writeLock.withLock {
                userStatsDao.insertOrUpdate(transform(userStatsDao.getOrInitUserStats()))
            }
        }


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

    override suspend fun storeSubscriptionTier(tier: SubscriptionTier) =
        updateStats { it.copy(subscriptionTier = tier) }

    override suspend fun fetchHideWelcomeOverlay(): Boolean = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().hideWelcomeOverlay
    }

    override suspend fun storeHideWelcomeOverlay(hide: Boolean) =
        updateStats { it.copy(hideWelcomeOverlay = hide) }

    override suspend fun storeDate() =
        updateStats { it.copy(lastPlayedTimestamp = Clock.System.now().toEpochMilliseconds()) }

    override suspend fun resetLivesAndStreak() =
        updateStats { it.copy(currentStreak = 0, livesPerType = emptyMap()) }

    override suspend fun storeAnswerStreak(answerStreak: Int) =
        updateStats { it.copy(currentStreak = answerStreak) }

    override suspend fun fetchAnswerStreak(): Int = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().currentStreak
    }

    override suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) =
        updateStats { it.copy(livesPerType = it.livesPerType + (quizType to numberOfLives)) }

    override suspend fun fetchCurrentLives(quizType: QuizType): Int = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().livesPerType[quizType] ?: STARTING_LIVES
    }

    override suspend fun fetchLastQuizType(): QuizType? = withContext(Dispatchers.Default) {
        userStatsDao.getOrInitUserStats().lastQuizType
    }

    override suspend fun storeLastQuizType(quizType: QuizType) =
        updateStats { it.copy(lastQuizType = quizType) }
}
