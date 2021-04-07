package com.team02.xgallery.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team02.xgallery.data.entity.LocalMedia
import com.team02.xgallery.data.entity.LocalMediaKey

@Database(entities = [LocalMedia::class, LocalMediaKey::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localMediaDao(): LocalMediaDao
    abstract fun localMediaKeyDao(): LocalMediaKeyDao
}