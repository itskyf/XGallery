package com.team02.xgallery.data.repository

import android.util.Log
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.data.source.network.CloudAlbumPagingSource
import com.team02.xgallery.ui.mediaincloudalbum.MediaInCloudAlbumFragmentDirections
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

    fun createAlbum(name: String, listItem: List<Any>) {
        val newCloudAlbum = CloudAlbum(
            id = Date().time.toString(),
            name = name,
            thumbnailId = listItem.first().toString()
        )
        val path = "albums/" + newCloudAlbum.id
        db.document(path).set(newCloudAlbum)
        for (item in listItem) {
            var arr: List<String> = item.toString().split("/")
            db.document(path).collection("media").document(arr.last())
                .set(hashMapOf("dateAdded" to Timestamp.now()))
        }
    }

    fun deleteAlbum(id: String) {
        db.document("albums/" + id)
            .delete()
            .addOnSuccessListener { Log.d("DeleteAlbum", "Deleting Successfully") }
            .addOnFailureListener { e -> Log.w("DeleteAlbum", "Deleting Failed", e) }
    }

    fun deletePhotos(id: String, listPhotos: List<Any>) {
        val subCollectRef = db.document("albums/" + id)
                                .collection("media")
        for (onePhoto in listPhotos) {
            var arr: List<String> = onePhoto.toString().split("/")
            subCollectRef.document(arr.last()).delete()
        }
        subCollectRef.get()
                .addOnSuccessListener { result ->
                    if (result.size() > 0 ) {
                        val newThumbnailId = Firebase.auth.currentUser?.uid.toString() + "/media/" + result.first().id
                        Log.d("Hieu",newThumbnailId)
                        db.document("albums/" + id).update("thumbnailId", newThumbnailId)
                    }
                    else {
                        deleteAlbum(id)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Fail", "Error getting documents: ", exception)
                }
    }
}