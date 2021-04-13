package com.team02.xgallery.utils

import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object AppConstants {
    val COLLECTION: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    const val ALBUM_PAGE_SIZE = 10

    const val GALLERY_PAGE_SIZE = 100
}