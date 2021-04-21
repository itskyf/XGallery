package com.team02.xgallery.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Entity
data class CloudMedia(
        @PrimaryKey val id: String? = null,     // TODO: reference to an media in Firebase Storage
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