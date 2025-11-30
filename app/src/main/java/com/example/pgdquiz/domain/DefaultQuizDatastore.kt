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


const val KEY_ANSWER_STREAK = "answer_streak"
const val KEY_LIVES = "_lives"
const val KEY_LAST_RESET_DATE = "last_reset_date"

class DefaultQuizDatastore(context: Context): QuizDatastore {

    private val prefs = context.getSharedPreferences("quiz_progress", Context.MODE_PRIVATE)

    var today: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun isTodayTheSameDayAsTheStoredDate(): Boolean {
        val lastStoredDate = prefs.getString(KEY_LAST_RESET_DATE, "")
        val todayIsDifferentStored = lastStoredDate != today
        if (todayIsDifferentStored) {
            resetLivesAndStreak()
        }
        return todayIsDifferentStored
    }

    override fun storeDate() {
        prefs.edit { putString(KEY_LAST_RESET_DATE, today) }
    }

    override fun resetLivesAndStreak() {
        QuizType.entries.forEach { type ->
            prefs.edit { putInt("${type.name}$KEY_LIVES", STARTING_LIVES) }
        }
        prefs.edit { putInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK) }

    }

    override fun storeAnswerStreak(answerStreak: Int) {
        prefs.edit { putInt(KEY_ANSWER_STREAK, answerStreak) }
    }

    override fun fetchAnswerStreak(): Int {
        return prefs.getInt(KEY_ANSWER_STREAK, STARTING_ANSWER_STREAK)
    }

    override fun storeCurrentLives(numberOfLives: Int, quizType: QuizType) {
        prefs.edit { putInt("${quizType.name}$KEY_LIVES", numberOfLives) }
    }

    override fun fetchCurrentLives(quizType: QuizType): Int {
        return prefs.getInt("${quizType.name}$KEY_LIVES", STARTING_LIVES)
    }
}