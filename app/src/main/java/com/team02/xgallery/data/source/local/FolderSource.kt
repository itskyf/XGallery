package com.team02.xgallery.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.team02.xgallery.Utils
import com.team02.xgallery.data.entity.Folder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("InlinedApi") // BUCKET_DISPLAY_NAME change interface but still ok for using
class FolderSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val projection = arrayOf(
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media._ID
    )
    private val collection =
        Utils.collection.buildUpon().appendQueryParameter("limit", "100").build()
    private val selection =
        "(${MediaStore.MediaColumns.BUCKET_DISPLAY_NAME}, ${MediaStore.MediaColumns.BUCKET_ID}) > (?, ?)"
    private val sortOrder =
        "${MediaStore.MediaColumns.BUCKET_DISPLAY_NAME} ASC, ${MediaStore.MediaColumns.BUCKET_ID} ASC"

    fun searchSomeFolders(startAfter: Pair<String, String>?): FolderQueryResponse {
        val folderIdSet = HashSet<String>()
        val folderList = mutableListOf<Folder>()
        var selectionArgs = if (startAfter != null) {
            arrayOf(startAfter.first, startAfter.second)
        } else {
            arrayOf("", "")
        }
        val query = context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val thumbnailIdColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getString(idColumn)
                val name = cursor.getString(nameColumn)
                if (cursor.isLast) {
                    selectionArgs = arrayOf(name, id)

                }
                if (folderIdSet.add(id)) {
                    folderList += Folder(id, name, cursor.getLong(thumbnailIdColumn))
                }
            }
        }
        return FolderQueryResponse(folderList, Pair(selectionArgs[0], selectionArgs[1]))
    }
}

data class FolderQueryResponse(
    val folders: List<Folder>,
    val lastQueriedItem: Pair<String, String>
)