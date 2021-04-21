package com.team02.xgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.source.network.CloudMediaPagingSource
import com.team02.xgallery.utils.AppConstants

class CloudMediaRepository {
    val pagingFlow = Pager(
            config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
            pagingSourceFactory = {
                CloudMediaPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString())
            }
    ).flow
}