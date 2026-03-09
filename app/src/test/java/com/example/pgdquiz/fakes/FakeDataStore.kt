package com.example.pgdquiz.fakes

import com.example.pgdquiz.domain.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_LIVES

class FakeDataStore : QuizDatastore {
    private var isSameDay = true
    private var livesPerType = mutableMapOf<QuizType, Int>()
    private var answerstreak = 0
    var isDateStored: Boolean = false

    private var lastQuizType: QuizType? = null

    override suspend fun fetchLastQuizType(): QuizType? {
        return lastQuizType
    }

override suspend fun storeLastQuizType(quizType: QuizType) {
    lastQuizType = quizType
}


    fun setIsSameDay(isSame: Boolean) {
        this.isSameDay = isSame
    }

    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return isSameDay
    }

    override fun storeDate() {
        isDateStored = true
    }

    override fun resetLivesAndStreak() {
        this.livesPerType.clear()
        this.answerstreak = 0
        isDateStored = true
    }

    override fun storeAnswerStreak(streak: Int) {
        this.answerstreak = streak
    }

    override suspend fun fetchAnswerStreak(): Int = answerstreak

    override suspend fun storeCurrentLives(lives: Int, quizType: QuizType) {
        livesPerType[quizType] = lives
    }

    override fun fetchCurrentLives(quizType: QuizType): Int {
    return livesPerType[quizType] ?: STARTING_LIVES }
}
