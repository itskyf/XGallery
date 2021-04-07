package com.team02.xgallery.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.team02.xgallery.Utils
import com.team02.xgallery.data.entity.LocalMedia
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@SuppressLint("InlinedApi") // BUCKET_DISPLAY_NAME change interface but still ok for using
class LocalMediaSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
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

    fun searchMediaByAlbum(albumId: String, pageSize: Int, startAfterId: Long?): List<LocalMedia> {
        val mediaList = mutableListOf<LocalMedia>()
        val selectionArgs = arrayOf(albumId, startAfterId?.toString() ?: "${Long.MAX_VALUE}")
        Timber.d("afterId ${selectionArgs[1]}")
        context.contentResolver.query(
            Utils.collection.buildUpon().appendQueryParameter("limit", "$pageSize").build(),
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

