package com.team02.xgallery.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.team02.xgallery.data.MediaRepository
import com.team02.xgallery.data.UserRepository
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

    @Provides
    @ViewModelScoped
    fun provideUserRepository() = UserRepository()
}