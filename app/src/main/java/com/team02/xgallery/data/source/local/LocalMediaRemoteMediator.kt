package com.team02.xgallery.data.source.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.team02.xgallery.data.entity.LocalMedia
import com.team02.xgallery.data.entity.LocalMediaKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class LocalMediaRemoteMediator(
    private val albumId: String,
    private val roomDb: AppDatabase,
    private val mediaSource: LocalMediaSource,
    private val dispatcher: CoroutineDispatcher
) : RemoteMediator<Int, LocalMedia>() {
    private val mediaDao = roomDb.localMediaDao()
    private val mediaKeyDao = roomDb.localMediaKeyDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalMedia>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = roomDb.withTransaction {
                        mediaKeyDao.remoteKeyByAlbumId(albumId)
                    }
                    remoteKey.currentPageKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            Timber.d("loadKey: $loadKey")
            Timber.d("loadType: ${loadType.name}")
            val mediaPage = withContext(dispatcher) {
                mediaSource.searchMediaByAlbum(albumId, state.config.pageSize, loadKey)
            }
            roomDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mediaKeyDao.deleteByAlbumId(albumId)
                    mediaDao.deleteByAlbum(albumId)
                }
                mediaKeyDao.insertOrReplace(
                    LocalMediaKey(
                        albumId,
                        mediaPage.lastOrNull()?.id
                    ) // TODO could I save next key?
                )
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
}