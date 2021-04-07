package com.team02.xgallery.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalMedia(
    @PrimaryKey override val id: Long,
    override val name: String,
    override val dateTaken: Long,
    override val albumId: String
) : Media