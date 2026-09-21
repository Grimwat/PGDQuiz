package com.tradezmantom.pgdquiz

import android.app.Application
import com.tradezmantom.pgdquiz.billing.BillingManager
import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.data.database.AppDatabase
import com.tradezmantom.pgdquiz.data.database.createAppDatabase
import com.tradezmantom.pgdquiz.data.repository.DefaultQuizDatastore
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore

class QuizApplication : Application() {

    val database: AppDatabase by lazy { createAppDatabase(PlatformContext(this)) }

    val quizDatastore: QuizDatastore by lazy {
        DefaultQuizDatastore(database.userStatsDao())
    }
    lateinit var billingManager: BillingManager

    override fun onCreate() {
        super.onCreate()
    }
}
