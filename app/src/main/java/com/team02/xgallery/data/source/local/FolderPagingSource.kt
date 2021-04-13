package com.team02.xgallery.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.team02.xgallery.data.entity.Folder
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

@SuppressLint("InlinedApi")
class FolderPagingSource(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : PagingSource<Int, Folder>() {
    override fun getRefreshKey(state: PagingState<Int, Folder>): Int? {
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Folder> {
        return try {
            val folderPage = withContext(dispatcher) {
                searchSomeFolders(params.loadSize, params.key)
            }
            LoadResult.Page(
                data = folderPage,
                prevKey = null, // only paging forward.
                nextKey = folderPage.lastOrNull()?.id
            )
        } catch (exception: Exception) {
            Timber.d("Exception: $exception")
            LoadResult.Error(exception)
        }
    }

    private val collection =
        AppConstants.COLLECTION.buildUpon().appendQueryParameter("limit", "1").build()
    private val projection = arrayOf(
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media._ID
    )
    private val selection = "${MediaStore.MediaColumns.BUCKET_ID} > ?"
    private val sortOrder = "${MediaStore.MediaColumns.BUCKET_ID} ASC"

    private fun searchSomeFolders(pageSize: Int, startAfter: Int?): List<Folder> {
        val folderList = mutableListOf<Folder>()
        var selectionArgs = arrayOf(startAfter?.toString() ?: "")
        repeat(pageSize) {
            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
            query?.use { cursor ->
                if (cursor.moveToNext()) {
                    val id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
                    )
                    selectionArgs = arrayOf(id.toString())
                    folderList += Folder(
                        id = id,
                        name = cursor.getString(
                            cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
                        ),
                        thumbnailId = cursor.getLong(
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        )
                    )
                } else {
                    return@repeat
                }
            }
        }
        return folderList
    }
}