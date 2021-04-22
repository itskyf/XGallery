package com.team02.xgallery.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Entity
data class CloudAlbum(
    @PrimaryKey val id: String? = null,
    val name: String? = null,
    val dateTaken: Timestamp? = Timestamp.now(),
    val thumbnailId: String? = null,
    val members: MutableList<String>? = mutableListOf<String>(Firebase.auth.currentUser?.uid.toString()),
)