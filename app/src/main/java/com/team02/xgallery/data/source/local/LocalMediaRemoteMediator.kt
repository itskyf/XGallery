package com.team02.xgallery.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.team02.xgallery.data.entity.LocalMedia
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPagingApi::class)
class LocalMediaRemoteMediator(
    private val albumId: Int,
    private val context: Context,
    private val roomDb: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) : RemoteMediator<Int, LocalMedia>() {
    private val mediaDao = roomDb.localMediaDao()

    //TODO override init method

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalMedia>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()?.id
            }
            Timber.d("loadKey: $loadKey")
            Timber.d("loadType: ${loadType.name}")
            val mediaPage = withContext(dispatcher) {
                searchMediaByAlbum(albumId, state.config.pageSize, loadKey)
            }
            roomDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mediaDao.deleteByAlbum(albumId)
                }
                mediaDao.insertAll(mediaPage)
            }
            Timber.d("loaded size: ${mediaPage.size}")
            MediatorResult.Success(
                endOfPaginationReached = mediaPage.isEmpty()
            )
        } catch (e: Exception) {
            Timber.d("Exception: $e")
            MediatorResult.Error(e)
        }
    }

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATE_TAKEN,
        MediaStore.MediaColumns.BUCKET_ID
    )
    private val selection =
        "${MediaStore.MediaColumns.BUCKET_ID} = ? AND ${MediaStore.Images.Media._ID} < ?"
    private val sortOrder =
        "${MediaStore.MediaColumns.DATE_TAKEN} DESC, ${MediaStore.Images.Media._ID} DESC"

    private fun searchMediaByAlbum(
        albumId: Int,
        pageSize: Int,
        startAfterId: Long?
    ): List<LocalMedia> {
        val mediaList = mutableListOf<LocalMedia>()
        val selectionArgs =
            arrayOf("$albumId", startAfterId?.toString() ?: "${Long.MAX_VALUE}")
        context.contentResolver.query(
            AppConstants.COLLECTION.buildUpon().appendQueryParameter("limit", "$pageSize").build(),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN)
            while (cursor.moveToNext()) {
                mediaList += LocalMedia(
                    id = cursor.getLong(idColumn),
                    name = cursor.getString(nameColumn),
                    dateTaken = cursor.getLong(dateTakenColumn),
                    albumId = albumId
                )
            }
        }
        return mediaList
    }
}