package com.team02.xgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.source.network.CloudMediaPagingSource
import com.team02.xgallery.data.source.network.DeletedCloudMediaPagingSource
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.tasks.await

class CloudMediaRepository {
    private val db = Firebase.firestore
    private val userUid = Firebase.auth.currentUser?.uid.toString()

    val allMediaPagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            CloudMediaPagingSource(db, Firebase.auth.currentUser?.uid.toString())
        }
    ).flow

    val deletedMediaPagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            DeletedCloudMediaPagingSource(db, userUid)
        }
    ).flow

    suspend fun updateState(mediaId: String, stateField: StateField, stateValue: Boolean) {
        db.collection("media")
            .whereEqualTo("id", mediaId)
            .get()
            .await()
            .documents[0].reference.update(stateField.value, stateValue).await()
    }

    suspend fun getState(mediaId: String, stateField: StateField) : Boolean {
        return db.collection("media")
            .whereEqualTo("id", mediaId)
            .get()
            .await()
            .documents[0].get(stateField.value) as Boolean
    }

    enum class StateField(val value: String) {
        FAVORITE("isFavorite"),
        ARCHIVED("isArchived"),
        DELETED("isDeleted")
    }
}