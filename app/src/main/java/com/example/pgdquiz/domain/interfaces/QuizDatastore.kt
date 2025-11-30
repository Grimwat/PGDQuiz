package com.example.pgdquiz.domain.interfaces

import com.example.pgdquiz.ui.data.QuizType

interface QuizDatastore {
    fun isTodayTheSameDayAsTheStoredDate(): Boolean
    fun storeDate()
    fun resetLivesAndStreak()
    fun storeAnswerStreak(answerStreak: Int)
    fun fetchAnswerStreak(): Int
    fun storeCurrentLives(numberOfLives: Int, quizType: QuizType)
    fun fetchCurrentLives(quizType: QuizType): Int
}