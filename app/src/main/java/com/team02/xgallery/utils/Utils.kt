package com.team02.xgallery.utils

import android.animation.TimeInterpolator
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.TransitionSet
import java.util.concurrent.atomic.AtomicInteger


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

    private val mNotificationCounter = AtomicInteger(1)
    val notificationCounter
        get() = mNotificationCounter.getAndAdd(2)

    inline fun transitionTogether(crossinline body: TransitionSet.() -> Unit): TransitionSet {
        return TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            body()
        }
    }

    val FAST_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
        PathInterpolatorCompat.create(0.4f, 0f, 0.2f, 1f)
    }

    fun setPhotoAs(context: Context, bitmap: Bitmap) {
        val myWallpaperManager = WallpaperManager.getInstance(context)
        val modes = arrayOf("Home Screen", "Lock Screen", "Home Screen and Lock Screen")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Set photo as")
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
            }
        }
        builder.show()
    }
}