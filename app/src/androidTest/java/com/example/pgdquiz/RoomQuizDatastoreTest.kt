package com.example.pgdquiz

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pgdquiz.data.database.AppDatabase
import com.example.pgdquiz.data.repository.RoomQuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.data.SubscriptionTier
import com.example.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RoomQuizDatastoreTest {

    private lateinit var database: AppDatabase
    private lateinit var datastore: RoomQuizDatastore

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        datastore = RoomQuizDatastore(database.userStatsDao())
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun fetchLivesReturnsStartingLivesWhenNotSet() = runTest {
        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.DRAIN_LAYING))
        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.GASFITTING))
    }

    @Test
    fun storeAndFetchLivesPlumbing() = runTest {
        datastore.storeCurrentLives(3, QuizType.PLUMBING)
        assertEquals(3, datastore.fetchCurrentLives(QuizType.PLUMBING))
    }

    @Test
    fun storeAndFetchLivesDrainLaying() = runTest {
        datastore.storeCurrentLives(2, QuizType.DRAIN_LAYING)
        assertEquals(2, datastore.fetchCurrentLives(QuizType.DRAIN_LAYING))
    }

    @Test
    fun storeAndFetchLivesGasFitting() = runTest {
        datastore.storeCurrentLives(1, QuizType.GASFITTING)
        assertEquals(1, datastore.fetchCurrentLives(QuizType.GASFITTING))
    }

    @Test
    fun livesAreIndependentPerQuizType() = runTest {
        datastore.storeCurrentLives(1, QuizType.PLUMBING)
        datastore.storeCurrentLives(2, QuizType.DRAIN_LAYING)
        datastore.storeCurrentLives(3, QuizType.GASFITTING)

        assertEquals(1, datastore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals(2, datastore.fetchCurrentLives(QuizType.DRAIN_LAYING))
        assertEquals(3, datastore.fetchCurrentLives(QuizType.GASFITTING))
    }

    @Test
    fun storeLivesOverwritesPreviousValue() = runTest {
        datastore.storeCurrentLives(5, QuizType.PLUMBING)
        datastore.storeCurrentLives(2, QuizType.PLUMBING)
        assertEquals(2, datastore.fetchCurrentLives(QuizType.PLUMBING))
    }


    @Test
    fun fetchStreakReturnsZeroWhenNotSet() = runTest {
        assertEquals(0, datastore.fetchAnswerStreak())
    }

    @Test
    fun storeAndFetchStreak() = runTest {
        datastore.storeAnswerStreak(7)
        assertEquals(7, datastore.fetchAnswerStreak())
    }

    @Test
    fun storeStreakOverwritesPreviousValue() = runTest {
        datastore.storeAnswerStreak(5)
        datastore.storeAnswerStreak(12)
        assertEquals(12, datastore.fetchAnswerStreak())
    }


    @Test
    fun resetLivesAndStreakClearsAllValues() = runTest {
        datastore.storeCurrentLives(2, QuizType.PLUMBING)
        datastore.storeCurrentLives(3, QuizType.DRAIN_LAYING)
        datastore.storeCurrentLives(1, QuizType.GASFITTING)
        datastore.storeAnswerStreak(10)

        datastore.resetLivesAndStreak()

        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.DRAIN_LAYING))
        assertEquals(STARTING_LIVES, datastore.fetchCurrentLives(QuizType.GASFITTING))
        assertEquals(0, datastore.fetchAnswerStreak())
    }


    @Test
    fun isTodayTheSameDayReturnsFalseOnFreshDatabase() = runTest {
        assertFalse(datastore.isTodayTheSameDayAsTheStoredDate())
    }

    @Test
    fun isTodayTheSameDayReturnsTrueAfterStoringDate() = runTest {
        datastore.storeDate()
        assertTrue(datastore.isTodayTheSameDayAsTheStoredDate())
    }


    @Test
    fun fetchLastQuizTypeReturnsNullWhenNotSet() = runTest {
        assertNull(datastore.fetchLastQuizType())
    }

    @Test
    fun storeAndFetchLastQuizType() = runTest {
        datastore.storeLastQuizType(QuizType.GASFITTING)
        assertEquals(QuizType.GASFITTING, datastore.fetchLastQuizType())
    }

    @Test
    fun storeLastQuizTypeOverwritesPreviousValue() = runTest {
        datastore.storeLastQuizType(QuizType.PLUMBING)
        datastore.storeLastQuizType(QuizType.DRAIN_LAYING)
        assertEquals(QuizType.DRAIN_LAYING, datastore.fetchLastQuizType())
    }


    @Test
    fun defaultSubscriptionTierIsFree() = runTest {
        assertEquals(SubscriptionTier.FREE, datastore.fetchSubscriptionTier())
    }

    @Test
    fun storeAndFetchApprenticeTier() = runTest {
        datastore.storeSubscriptionTier(SubscriptionTier.APPRENTICE)
        assertEquals(SubscriptionTier.APPRENTICE, datastore.fetchSubscriptionTier())
    }

    @Test
    fun storeAndFetchTradesmanTier() = runTest {
        datastore.storeSubscriptionTier(SubscriptionTier.TRADESMAN)
        assertEquals(SubscriptionTier.TRADESMAN, datastore.fetchSubscriptionTier())
    }

    @Test
    fun storeTierOverwritesPreviousValue() = runTest {
        datastore.storeSubscriptionTier(SubscriptionTier.APPRENTICE)
        datastore.storeSubscriptionTier(SubscriptionTier.TRADESMAN)
        assertEquals(SubscriptionTier.TRADESMAN, datastore.fetchSubscriptionTier())
    }

    @Test
    fun tierDowngradeFromTradesmanToFree() = runTest {
        datastore.storeSubscriptionTier(SubscriptionTier.TRADESMAN)
        datastore.storeSubscriptionTier(SubscriptionTier.FREE)
        assertEquals(SubscriptionTier.FREE, datastore.fetchSubscriptionTier())
    }


    @Test
    fun allDataPersistsIndependently() = runTest {
        datastore.storeCurrentLives(3, QuizType.PLUMBING)
        datastore.storeAnswerStreak(8)
        datastore.storeLastQuizType(QuizType.GASFITTING)
        datastore.storeSubscriptionTier(SubscriptionTier.APPRENTICE)
        datastore.storeDate()

        assertEquals(3, datastore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals(8, datastore.fetchAnswerStreak())
        assertEquals(QuizType.GASFITTING, datastore.fetchLastQuizType())
        assertEquals(SubscriptionTier.APPRENTICE, datastore.fetchSubscriptionTier())
        assertTrue(datastore.isTodayTheSameDayAsTheStoredDate())
    }
}