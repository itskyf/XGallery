package com.team02.xgallery.data.source.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.team02.xgallery.data.entity.Folder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class FolderPagingSource(
    private val folderSource: FolderSource,
    private val dispatcher: CoroutineDispatcher
) : PagingSource<Pair<String, String>, Folder>() {
    override fun getRefreshKey(state: PagingState<Pair<String, String>, Folder>): Pair<String, String>? =
        state.lastItemOrNull()?.let {
            Pair(it.name, it.id)
        }

    override suspend fun load(params: LoadParams<Pair<String, String>>): LoadResult<Pair<String, String>, Folder> {
        return try {
            val folderList = mutableListOf<Folder>()
            var startAfter = params.key
            do {
                val response = withContext(dispatcher) {
                    folderSource.searchSomeFolders(startAfter = startAfter)
                }
                folderList += response.folders
                startAfter = response.lastQueriedItem
            } while (folderList.size < params.loadSize && response.folders.isNotEmpty())
            LoadResult.Page(
                data = folderList,
                prevKey = null, // Only paging forward.
                nextKey = if (folderList.isEmpty()) null else startAfter

            )
        } catch (exception: Exception) {
            Timber.d("Exception: $exception")
            LoadResult.Error(exception)
        }
    }
}