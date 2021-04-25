package com.team02.xgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.data.source.network.CloudAlbumPagingSource
import com.team02.xgallery.utils.AppConstants
import java.util.*

class CloudAlbumRepository {
    val pagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            CloudAlbumPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString())
        }
    ).flow

    private val db = Firebase.firestore

    fun createAlbum(name: String) {
        val newCloudAlbum = CloudAlbum(
            name = name,
            thumbnailId = "pxvXcgeCETcVGBKz1vIl1KMLEqB2/media/0b44d29c-904a-45a0-94ac-b859859dd5f420200722_160350.jpg"
        )
        db.document("albums/${Date().time}").set(newCloudAlbum)
    }
}