package com.tradezmantom.pgdquiz.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.tradezmantom.pgdquiz.billing.PlatformContext
import kotlinx.coroutines.Dispatchers

expect fun createDatabaseBuilder(platformContext: PlatformContext): RoomDatabase.Builder<AppDatabase>

fun createAppDatabase(platformContext: PlatformContext): AppDatabase {
    return createDatabaseBuilder(platformContext)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}
