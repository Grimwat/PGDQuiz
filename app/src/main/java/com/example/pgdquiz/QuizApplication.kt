package com.example.pgdquiz

import android.app.Application
import androidx.room.Room
import com.example.pgdquiz.data.database.AppDatabase
import com.example.pgdquiz.data.repository.RoomQuizDatastore
import com.example.pgdquiz.domain.interfaces.QuizDatastore


class QuizApplication : Application() {


    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quiz_database"
        ).build()
    }

    val quizDatastore: QuizDatastore by lazy {
        RoomQuizDatastore(database.userStatsDao())
    }
}
