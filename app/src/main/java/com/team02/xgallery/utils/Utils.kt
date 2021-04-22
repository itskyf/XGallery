package com.team02.xgallery.utils

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
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

    fun setDevicePhotoAs(context: Context, mediaUri: Uri) {
        val bitmap =
            MediaStore.Images.Media.getBitmap(context.contentResolver, mediaUri)
        val myWallpaperManager = WallpaperManager.getInstance(context)
        val modes = arrayOf("Home Screen", "Lock Screen", "Home Screen and Lock Screen", "Avatar")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Set Wallpaper")
        builder.setItems(modes) { _, which ->
            when (which) {
                0 -> myWallpaperManager.setBitmap(
                        bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_SYSTEM
                )
                1 -> myWallpaperManager.setBitmap(
                        bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK
                )
                2 -> {
                    myWallpaperManager.setBitmap(
                            bitmap,
                            null,
                            true,
                            WallpaperManager.FLAG_SYSTEM
                    )
                    myWallpaperManager.setBitmap(
                            bitmap,
                            null,
                            true,
                            WallpaperManager.FLAG_LOCK
                    )
                }
                3 -> {
                    val user = Firebase.auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = "Kiều Công Hậu"
                        photoUri = mediaUri
                    }
                    user!!.updateProfile(profileUpdates)
                }
            }
        }
        builder.show()
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