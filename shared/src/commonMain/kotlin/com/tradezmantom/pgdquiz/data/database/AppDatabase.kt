package com.tradezmantom.pgdquiz.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.tradezmantom.pgdquiz.data.database.dao.UserStatsDao
import com.tradezmantom.pgdquiz.data.database.entities.UserStatsEntity

const val DATABASE_NAME = "quiz_database"

@Database(entities = [UserStatsEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userStatsDao(): UserStatsDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE user_stats ADD COLUMN subscriptionTier TEXT NOT NULL DEFAULT 'FREE'"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE user_stats ADD COLUMN hideWelcomeOverlay INTEGER NOT NULL DEFAULT 0"
        )
    }
}
