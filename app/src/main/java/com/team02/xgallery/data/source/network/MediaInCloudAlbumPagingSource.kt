package com.team02.xgallery.data.source.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firestore.v1.Value
import com.team02.xgallery.data.entity.CloudMedia
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.tasks.await
import kotlin.reflect.typeOf


class MediaInCloudAlbumPagingSource(
    private val db: FirebaseFirestore,
    private val userUID: String,
    private val albumUID: String,
) : PagingSource<QuerySnapshot, CloudMedia>() {

    override suspend fun load(params: PagingSource.LoadParams<QuerySnapshot>): PagingSource.LoadResult<QuerySnapshot, CloudMedia> {
        return try {
            var listID : MutableList<String> = mutableListOf()
            db.collection("albums").document(albumUID).collection("media").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        listID.add(userUID + "/media/" + document.id)
                    }
                }.await()

            val chunkedID = listID.chunked(9)

            var snapshotvar: QuerySnapshot = db.collection("media")
                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                .get()
                .await()

            var Data: MutableList<CloudMedia> = arrayListOf()
            for (oneList in chunkedID) {
                var tempPage = db.collection("media")
                    .whereIn("id", oneList)
                    .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                    .get()
                    .await()
                Data = (Data + (tempPage.toObjects(CloudMedia::class.java))) as MutableList<CloudMedia>
                snapshotvar = tempPage
            }

            val currentPage = params.key
                ?:snapshotvar
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

//            val nextPage = db.collection("media")
//                .whereIn("id", listID)
//                .startAfter(lastDocumentSnapshot)
//                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
//                .get()
//                .await()

            PagingSource.LoadResult.Page(
                data = Data,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            Log.d("NDH", "$e")
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CloudMedia>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}