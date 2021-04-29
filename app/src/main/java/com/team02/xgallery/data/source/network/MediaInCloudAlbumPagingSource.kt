package com.team02.xgallery.data.source.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.team02.xgallery.data.entity.CloudMedia
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.tasks.await

class MediaInCloudAlbumPagingSource (
private val db: FirebaseFirestore,
private val userUID: String
) : PagingSource<QuerySnapshot, CloudMedia>() {

    override suspend fun load(params: PagingSource.LoadParams<QuerySnapshot>): PagingSource.LoadResult<QuerySnapshot, CloudMedia> {
        return try {
            var listID : MutableList<String> = mutableListOf()
            db.collection("albums").document("1619627460782").collection("media").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        listID.add("pxvXcgeCETcVGBKz1vIl1KMLEqB2/media/" + document.id)
                    }
                }.await()

            val currentPage = params.key
                ?: db.collection("media")
                    .whereIn("id", listID)
                    .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                    .get()
                    .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("media")
                .whereIn("id", listID)
                .startAfter(lastDocumentSnapshot)
                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                .get()
                .await()

            PagingSource.LoadResult.Page(
                data = currentPage.toObjects(CloudMedia::class.java),
                prevKey = null,
                nextKey = nextPage
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