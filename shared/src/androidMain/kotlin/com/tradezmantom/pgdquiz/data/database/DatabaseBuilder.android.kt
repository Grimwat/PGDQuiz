package com.tradezmantom.pgdquiz.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.tradezmantom.pgdquiz.billing.PlatformContext

actual fun createDatabaseBuilder(platformContext: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    val appContext = platformContext.context.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
