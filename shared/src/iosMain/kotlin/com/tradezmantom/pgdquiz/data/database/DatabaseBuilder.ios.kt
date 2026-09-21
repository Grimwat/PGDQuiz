package com.tradezmantom.pgdquiz.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.tradezmantom.pgdquiz.billing.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun createDatabaseBuilder(platformContext: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbFilePath = requireNotNull(documentDirectory?.path) + "/$DATABASE_NAME"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
}
