package com.example.pgdquiz.fakes

import com.example.pgdquiz.network.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType

class FakeDataStore: QuizDatastore {
    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        return true
    }

    override fun storeDate() {
    }

    override fun resetLivesAndStreak() {
    }

    override fun storeAnswerStreak(answerStreak: Int) {
    }

    override fun fetchAnswerStreak(): Int {
        return 0
    }

    override fun storeCurrentLives(
        numberOfLives: Int,
        quizType: QuizType
    ) {
    }

    override fun fetchCurrentLives(quizType: QuizType): Int {
        return 5

    }
}