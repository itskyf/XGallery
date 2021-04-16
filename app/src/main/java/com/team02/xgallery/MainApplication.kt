package com.team02.xgallery

import android.app.Application
import android.content.pm.ApplicationInfo
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.team02.xgallery.utils.StorageReferenceFetcher
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader() = ImageLoader.Builder(applicationContext)
        .availableMemoryPercentage(.35)
        .crossfade(true)
        .apply {
            componentRegistry {
                add(StorageReferenceFetcher())
            }
        }
        .build()
}