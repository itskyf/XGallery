package com.team02.xgallery.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalMediaKey(
    @PrimaryKey val albumId: String,
    val currentPageKey: Long?
)