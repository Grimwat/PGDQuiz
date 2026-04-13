package com.example.pgdquiz

import android.app.Application
import androidx.room.Room
import com.example.pgdquiz.data.database.AppDatabase
import com.example.pgdquiz.data.repository.RoomQuizDatastore
import com.example.pgdquiz.domain.interfaces.QuizDatastore

class QuizApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val quizDatastore: QuizDatastore by lazy {
        RoomQuizDatastore(database.userStatsDao())
    }

    override fun onCreate() {
        super.onCreate()
    }
}