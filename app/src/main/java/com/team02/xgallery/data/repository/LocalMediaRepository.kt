package com.team02.xgallery.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.team02.xgallery.Utils
import com.team02.xgallery.data.source.local.AppDatabase
import com.team02.xgallery.data.source.local.LocalMediaRemoteMediator
import com.team02.xgallery.data.source.local.LocalMediaSource
import kotlinx.coroutines.CoroutineDispatcher

class LocalMediaRepository(
    private val roomDb: AppDatabase,
    private val mediaSource: LocalMediaSource,
    private val dispatcher: CoroutineDispatcher
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagingFlow(albumId: String) = Pager(
        config = PagingConfig(pageSize = Utils.GALLERY_PAGE_SIZE),
        remoteMediator = LocalMediaRemoteMediator(albumId, roomDb, mediaSource, dispatcher)
    ) {
        roomDb.localMediaDao().pagingSource(albumId)
    }.flow
}