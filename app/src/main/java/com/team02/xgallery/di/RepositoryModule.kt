package com.team02.xgallery.di

import com.team02.xgallery.data.repository.FolderRepository
import com.team02.xgallery.data.repository.LocalMediaRepository
import com.team02.xgallery.data.repository.UserRepository
import com.team02.xgallery.data.source.local.AppDatabase
import com.team02.xgallery.data.source.local.FolderSource
import com.team02.xgallery.data.source.local.LocalMediaSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
    fun provideFolderRepository(folderSource: FolderSource) =
        FolderRepository(folderSource, Dispatchers.IO)

    @Provides
    @ViewModelScoped
    fun provideLocalMediaRepository(roomDb: AppDatabase, mediaSource: LocalMediaSource) =
        LocalMediaRepository(roomDb, mediaSource, Dispatchers.IO)
}