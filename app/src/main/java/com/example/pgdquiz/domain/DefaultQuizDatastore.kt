package com.example.pgdquiz.domain

import android.content.Context
import com.example.pgdquiz.domain.interfaces.QuizDatastore
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_ANSWER_STREAK
import com.example.pgdquiz.util.STARTING_LIVES
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


const val KEY_ANSWER_STREAK = "answer_streak"
const val KEY_LIVES = "_lives"
const val KEY_LAST_RESET_DATE = "last_reset_date"
const val KEY_LAST_QUIZ_TYPE = "last_quiz_type"


class DefaultQuizDatastore(context: Context): QuizDatastore {

    private val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)

    private fun getTodayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override suspend fun isTodayTheSameDayAsTheStoredDate(): Boolean =
        withContext(Dispatchers.IO) {
        val today = getTodayString()
        val lastStoredDate = prefs.getString(KEY_LAST_RESET_DATE, "")

        val isSameDay = lastStoredDate == today

        if (!isSameDay) {
            resetLivesAndStreak()
        }
        return@withContext isSameDay
    }

    override suspend fun storeDate() = withContext(Dispatchers.IO) {
        prefs.edit(commit = true) { putString(KEY_LAST_RESET_DATE, getTodayString()) }
    }

    override suspend fun resetLivesAndStreak() = withContext(Dispatchers.IO) {
        QuizType.entries.forEach { type ->
            prefs.edit { putInt("${type.name}$KEY_LIVES", STARTING_LIVES) }
        }
        prefs.edit { putInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK) }

    }

    override suspend fun storeAnswerStreak(answerStreak: Int) = withContext(Dispatchers.IO) {
        prefs.edit { putInt(KEY_ANSWER_STREAK, answerStreak) }
    }

    override suspend fun fetchAnswerStreak(): Int = withContext(Dispatchers.IO) {
        return@withContext prefs.getInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK)
    }

    override suspend fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) = withContext(Dispatchers.IO) {
        prefs.edit { putInt("${quizType.name}$KEY_LIVES", numberOfLives) }
    }

    override suspend fun fetchCurrentLives(quizType: QuizType): Int = withContext(Dispatchers.IO) {
        return@withContext prefs.getInt("${quizType.name}$KEY_LIVES", STARTING_LIVES)
    }
    override suspend fun fetchLastQuizType(): QuizType? = withContext(Dispatchers.IO) {
        val typeName = prefs.getString(KEY_LAST_QUIZ_TYPE, null)
        return@withContext typeName?. let { QuizType.valueOf(it) }
    }

    override suspend fun storeLastQuizType(quizType: QuizType) = withContext(Dispatchers.IO)  {
        prefs.edit { putString(KEY_LAST_QUIZ_TYPE, quizType.name) }

    }
}