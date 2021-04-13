package com.team02.xgallery.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.firebase.storage.StorageReference

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
}

inline fun ImageView.load(
    data: StorageReference,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable {
    val request = ImageRequest.Builder(context).data(data).target(this@load)
        .apply(builder).build()
    return context.imageLoader.enqueue(request)
}