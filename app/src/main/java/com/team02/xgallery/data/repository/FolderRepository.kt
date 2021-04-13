package com.team02.xgallery.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.team02.xgallery.data.source.local.FolderPagingSource
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.CoroutineDispatcher

class FolderRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) {
    val pagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            FolderPagingSource(context, dispatcher)
        }
    ).flow
}