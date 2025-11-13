package com.example.pgdquiz.fakes

import com.example.pgdquiz.network.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_ANSWER_STREAK
import com.example.pgdquiz.util.STARTING_LIVES

class FakeDataStore : QuizDatastore {
    private var isSameDay = true
    private var lives = STARTING_LIVES
    private var streak = STARTING_ANSWER_STREAK

    fun setIsSameDay(isSame: Boolean) {
        this.isSameDay = isSame
    }

    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return isSameDay
    }

    override fun storeDate() {
    }

    override fun resetLivesAndStreak() {
        this.lives = STARTING_LIVES
        this.streak = STARTING_ANSWER_STREAK
    }

    override fun storeAnswerStreak(streak: Int) {
        this.streak = streak
    }

    override fun fetchAnswerStreak(): Int = streak

    override fun storeCurrentLives(lives: Int, quizType: QuizType) {
        this.lives = lives
    }

    override fun fetchCurrentLives(quizType: QuizType): Int = lives
}
