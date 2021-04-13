package com.team02.xgallery.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team02.xgallery.data.entity.LocalMedia

@Database(entities = [LocalMedia::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localMediaDao(): LocalMediaDao
}