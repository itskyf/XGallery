package com.team02.xgallery.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalMedia(
    @PrimaryKey val id: Long,
    val name: String,
    val dateTaken: Long,
    val albumId: Int
)