package com.example.pgdquiz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pgdquiz.data.database.dao.UserStatsDao
import com.example.pgdquiz.data.database.entities.UserStatsEntity


@Database(entities = [UserStatsEntity::class], version = 1)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userStatsDao(): UserStatsDao
}
