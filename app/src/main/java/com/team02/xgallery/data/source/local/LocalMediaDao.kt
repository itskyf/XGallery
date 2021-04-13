package com.team02.xgallery.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team02.xgallery.data.entity.LocalMedia

@Dao
interface LocalMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<LocalMedia>)

    @Query("SELECT * FROM LocalMedia WHERE albumId = :albumId ORDER BY dateTaken DESC, id DESC")
    fun pagingSource(albumId: Int): PagingSource<Int, LocalMedia>

    @Query("DELETE FROM LocalMedia WHERE albumId = :albumId")
    suspend fun deleteByAlbum(albumId: Int)

    @Query("DELETE FROM LocalMedia")
    suspend fun clearAll()
}