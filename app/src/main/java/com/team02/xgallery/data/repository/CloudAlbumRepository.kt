package com.team02.xgallery.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.data.source.network.CloudMediaPagingSource
import com.team02.xgallery.utils.AppConstants

class CloudAlbumRepository {
    val pagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            CloudMediaPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString())
        }
    ).flow

    private val  db = Firebase.firestore


    fun createAlbum() {
        val refAlbumDoc = db.collection("albums").document()
        val roles : HashMap<String,String> = HashMap()
        roles.put(Firebase.auth.currentUser.uid.toString(),"owner")
        val newAlbum = CloudAlbum(refAlbumDoc.id,"test", Timestamp.now(),roles,"pxvXcgeCETcVGBKz1vIl1KMLEqB2/media/0b44d29c-904a-45a0-94ac-b859859dd5f420200722_160350.jpg")
        Log.d("hello","$newAlbum")
//        val test = hashMapOf(
//            "id" to newAlbum.id,
//            "name" to newAlbum.name,
//            "thumbnailId" to newAlbum.thumbnailId
//        )

        refAlbumDoc
            .set(newAlbum)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

    }
}