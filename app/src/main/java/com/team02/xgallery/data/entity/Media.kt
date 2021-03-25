package com.team02.xgallery.data.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class MyMedia(
    @DocumentId
    val id: String? = null,
    val uri: String? = null,
    val dateAdded: Timestamp? = null
) {
    val isOnlineItem: Boolean
        get() = uri?.contains('/') == false
    // TODO a better check
}