package com.team02.xgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.Timestamp
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
        val path = "albums/${Date().time}"
        db.document(path).set(newCloudAlbum)
        db.document(path).collection("media").document("1020bb94-3570-4484-adc4-00280c25ee2620200605_153043.jpg").set(hashMapOf("dateAdded" to Timestamp.now()))
    }

    fun createAlbum(name: String, listItem: List<Any>) {
        val newCloudAlbum = CloudAlbum(
            name = name,
            thumbnailId = "pxvXcgeCETcVGBKz1vIl1KMLEqB2/media/0b44d29c-904a-45a0-94ac-b859859dd5f420200722_160350.jpg"
        )
        val path = "albums/${Date().time}"
        db.document(path).set(newCloudAlbum)
        for (item in listItem) {
            var arr:List<String> = item.toString().split("/")
            db.document(path).collection("media").document(arr.last()).set(hashMapOf("dateAdded" to Timestamp.now()))
        }
    }
}