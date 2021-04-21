package com.team02.xgallery.di

import android.content.Context
import androidx.work.WorkManager
import com.team02.xgallery.data.repository.CloudMediaRepository
import com.team02.xgallery.data.repository.FolderRepository
import com.team02.xgallery.data.repository.LocalMediaRepository
import com.team02.xgallery.data.repository.UserRepository
import com.team02.xgallery.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideUserRepository() = UserRepository()

    @Provides
    @ViewModelScoped
    fun provideFolderRepository(@ApplicationContext context: Context) =
        FolderRepository(context, Dispatchers.IO)

    @Provides
    @ViewModelScoped
    fun provideLocalMediaRepository(@ApplicationContext context: Context, roomDb: AppDatabase) =
        LocalMediaRepository(context, roomDb, Dispatchers.IO)

    @Provides
    @ViewModelScoped
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @ViewModelScoped
    fun provideCloudPhotoRepository() = CloudMediaRepository()
}