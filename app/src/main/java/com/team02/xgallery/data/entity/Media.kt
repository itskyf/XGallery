package com.team02.xgallery.data.entity

interface Media {
    val id: Any
    val name: String
    val albumId: String
    val dateTaken: Long
    override fun equals(other: Any?): Boolean
}