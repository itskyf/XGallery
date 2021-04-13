package com.team02.xgallery.data.entity

data class Folder(
    override val id: Int,
    override val name: String,
    override val thumbnailId: Long
) : Album
