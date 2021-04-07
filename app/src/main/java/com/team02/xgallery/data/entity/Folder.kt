package com.team02.xgallery.data.entity

data class Folder(
    override val id: String, // cause it has nothing than for query
    override val name: String,
    override val thumbnailId: Long
) : Album
