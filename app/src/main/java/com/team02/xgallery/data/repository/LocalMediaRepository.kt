package com.team02.xgallery.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.team02.xgallery.data.source.local.AppDatabase
import com.team02.xgallery.data.source.local.LocalMediaRemoteMediator
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.CoroutineDispatcher

class LocalMediaRepository(
    private val context: Context,
    private val roomDb: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagingFlow(albumId: Int) = Pager(
        config = PagingConfig(pageSize = AppConstants.GALLERY_PAGE_SIZE),
        remoteMediator = LocalMediaRemoteMediator(albumId, context, roomDb, dispatcher)
    ) {
        roomDb.localMediaDao().pagingSource(albumId)
    }.flow
}