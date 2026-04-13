package com.example.pgdquiz.domain.interfaces

import com.example.pgdquiz.ui.data.QuizType

interface QuizDatastore {
    suspend fun isTodayTheSameDayAsTheStoredDate(): Boolean
    suspend fun storeDate()
    suspend fun resetLivesAndStreak()
    suspend fun storeAnswerStreak(answerStreak: Int)
    suspend fun fetchAnswerStreak(): Int
    suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType)
    suspend fun fetchLastQuizType(): QuizType?
    suspend fun storeLastQuizType(quizType: QuizType)
    suspend fun fetchCurrentLives(quizType: QuizType): Int
}