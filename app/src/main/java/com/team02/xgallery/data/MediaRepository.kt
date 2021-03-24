package com.team02.xgallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.team02.xgallery.data.entity.Media
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val db: FirebaseFirestore) {
    fun getOnlinePagingFlow(): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(pageSize = 50),
            pagingSourceFactory = { OnlineMediaPagingSource(db) },
        ).flow
    }
}