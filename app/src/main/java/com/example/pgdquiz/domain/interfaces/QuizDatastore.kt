package com.example.pgdquiz.domain.interfaces

import com.example.pgdquiz.ui.data.QuizType

interface QuizDatastore {
    fun isTodayTheSameDayAsTheStoredDate(): Boolean
    fun storeDate()
    fun resetLivesAndStreak()
    fun storeAnswerStreak(answerStreak: Int)
    suspend fun fetchAnswerStreak(): Int
    suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType)
    suspend fun fetchLastQuizType(): QuizType?
    suspend fun storeLastQuizType(quizType: QuizType)
    fun fetchCurrentLives(quizType: QuizType): Int
}