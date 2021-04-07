package com.team02.xgallery.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team02.xgallery.data.entity.LocalMediaKey

@Dao
interface LocalMediaKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(key: LocalMediaKey)

    @Query("SELECT * FROM LocalMediaKey WHERE albumId = :albumId")
    suspend fun remoteKeyByAlbumId(albumId: String): LocalMediaKey

    @Query("DELETE FROM LocalMediaKey WHERE albumId = :albumId")
    suspend fun deleteByAlbumId(albumId: String)
}