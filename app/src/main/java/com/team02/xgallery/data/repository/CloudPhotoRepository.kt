package com.team02.xgallery.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CloudPhotoRepository {
    private val db = Firebase.firestore

    suspend fun updateFavoriteState(mediaId: String, state: Boolean) {
        db.collection("media")
            .whereEqualTo("id", mediaId)
            .get()
            .await()
            .documents[0].reference.update("isFavorite", state).await()
    }

    suspend fun getFavoriteState(mediaId: String) : Boolean {
        return db.collection("media")
            .whereEqualTo("id", mediaId)
            .get()
            .await()
            .documents[0].get("isFavorite") as Boolean
    }
}