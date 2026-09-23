package com.tradezmantom.pgdquiz.fakes

import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.util.STARTING_LIVES

class FakeDataStore : QuizDatastore {
    private var isSameDay = true
    private var livesPerType = mutableMapOf<QuizType, Int>()
    private var answerStreak = 0
    private var lastQuizType: QuizType? = null
    private var subscriptionTier = SubscriptionTier.FREE
    private var hideWelcomeOverlay = false
    var isDateStored: Boolean = false

    fun setIsSameDay(isSame: Boolean) {
        this.isSameDay = isSame
    }

    override suspend fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return isSameDay
    }

    override suspend fun storeDate() {
        isDateStored = true
    }

    override suspend fun resetLivesAndStreak() {
        this.livesPerType.clear()
        this.answerStreak = 0
        isDateStored = true
    }

    override suspend fun storeAnswerStreak(streak: Int) {
        this.answerStreak = streak
    }

    override suspend fun fetchAnswerStreak(): Int = answerStreak

    override suspend fun storeCurrentLives(lives: Int, quizType: QuizType) {
        livesPerType[quizType] = lives
    }

    override suspend fun fetchCurrentLives(quizType: QuizType): Int {
        return livesPerType[quizType] ?: STARTING_LIVES
    }

    override suspend fun fetchLastQuizType(): QuizType? {
        return lastQuizType
    }

    override suspend fun storeLastQuizType(quizType: QuizType) {
        lastQuizType = quizType
    }

    override suspend fun fetchSubscriptionTier(): SubscriptionTier = subscriptionTier

    override suspend fun storeSubscriptionTier(tier: SubscriptionTier) {
        subscriptionTier = tier
    }

    override suspend fun fetchHideWelcomeOverlay(): Boolean = hideWelcomeOverlay

    override suspend fun storeHideWelcomeOverlay(hide: Boolean) {
        hideWelcomeOverlay = hide
    }
}