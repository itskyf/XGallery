package com.team02.xgallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team02.xgallery.entity.Media

@Database(entities = [Media::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}