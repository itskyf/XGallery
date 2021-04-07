package com.team02.xgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.team02.xgallery.Utils
import com.team02.xgallery.data.entity.Album
import com.team02.xgallery.data.source.local.FolderPagingSource
import com.team02.xgallery.data.source.local.FolderSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterIsInstance

class FolderRepository(
    private val folderSource: FolderSource,
    private val dispatcher: CoroutineDispatcher
) {
    val pagingFlow = Pager(
        config = PagingConfig(pageSize = Utils.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            FolderPagingSource(folderSource, dispatcher)
        }
    ).flow.filterIsInstance<PagingData<Album>>()
    // TODO filter where
}