package com.team02.xgallery.data.entity

interface Album {
    val id: String
    val name: String
    val thumbnailId: Any
    override fun equals(other: Any?): Boolean
}
