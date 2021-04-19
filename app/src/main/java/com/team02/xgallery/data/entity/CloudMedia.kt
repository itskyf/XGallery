package com.team02.xgallery.data.entity

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class CloudMedia(
        val dateTaken: Timestamp? = Timestamp.now(),
        val fileName: String? = null,
        @field:JvmField
        val isArchived: Boolean? = false,
        @field:JvmField
        val isDeleted: Boolean? = false,
        @field:JvmField
        val isFavorite: Boolean? = false,
        val owner: String? = Firebase.auth.currentUser?.uid
)