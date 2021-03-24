package com.team02.xgallery.di

import com.google.firebase.firestore.FirebaseFirestore
import com.team02.xgallery.data.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideMediaRepository(db: FirebaseFirestore) = MediaRepository(db)
}