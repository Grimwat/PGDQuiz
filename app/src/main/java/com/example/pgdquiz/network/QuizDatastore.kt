package com.example.pgdquiz.network

import android.content.Context
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_ANSWER_STREAK
import com.example.pgdquiz.util.STARTING_LIVES
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val KEY_ANSWER_STREAK = "answer_streak"
const val KEY_LIVES = "_lives"
const val KEY_LAST_RESET_DATE = "last_reset_date"

class QuizDatastore(context: Context) {

    private val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)

    var today: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        val lastStoredDate = prefs.getString(KEY_LAST_RESET_DATE, "")
        val todayIsDifferentStored = lastStoredDate != today
        if (todayIsDifferentStored) {
            resetLivesAndStreak()
        }
        return todayIsDifferentStored
    }

    fun storeDate() {
        prefs.edit().putString(KEY_LAST_RESET_DATE, today)
    }

    fun resetLivesAndStreak() {
        QuizType.entries.forEach { type ->
            prefs.edit().putInt("${type.name}$KEY_LIVES", STARTING_LIVES)
        }
        prefs.edit().putInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK).apply()

    }

    fun storeAnswerStreak(answerStreak: Int) {
        prefs.edit().putInt(KEY_ANSWER_STREAK, answerStreak).apply()
    }

    fun fetchAnswerStreak(): Int {
        return prefs.getInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK)
    }

    fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) {
        prefs.edit().putInt("${quizType.name}$KEY_LIVES", numberOfLives).apply()
    }

    fun fetchCurrentLives(quizType: QuizType): Int {
        return prefs.getInt("${quizType.name}$KEY_LIVES", STARTING_LIVES)
    }
}