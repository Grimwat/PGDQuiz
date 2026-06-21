package com.tradezmantom.pgdquiz.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tradezmantom.pgdquiz.data.database.entities.UserStatsEntity

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 0")
    suspend fun getUserStats(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stats: UserStatsEntity)

    suspend fun getOrInitUserStats(): UserStatsEntity {
        return getUserStats() ?: UserStatsEntity().also { insertOrUpdate(it) }
    }
}
