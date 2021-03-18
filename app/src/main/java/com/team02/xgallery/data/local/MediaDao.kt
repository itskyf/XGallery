package com.team02.xgallery.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MediaDao {
    @Query("DELETE FROM media")
    suspend fun deleteMediaCache()
}