package com.team02.xgallery

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup

object Utils {
    fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child: View = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }

    val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    const val ALBUM_PAGE_SIZE = 10

    const val GALLERY_PAGE_SIZE = 50
}