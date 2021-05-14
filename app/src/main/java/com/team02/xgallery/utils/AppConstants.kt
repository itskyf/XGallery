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
    const val GALLERY_PAGE_SIZE = 50

    // Workers key & tag name
    const val WORKER_URI = "workerImageUri"
    const val WORKER_UUID = "workerFirebaseUuid"
    const val WORKER_NOTIFICATION_ID = "notificationId"
    const val WORKER_UPLOAD_TAG = "workerTag"

    // Notification related
    const val UPLOAD_CHANNEL_ID = "uploadGroup"
    const val UPLOAD_GROUP = "uploadGroup"
}