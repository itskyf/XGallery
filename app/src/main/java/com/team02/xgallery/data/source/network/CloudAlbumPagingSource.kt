package com.team02.xgallery.data.source.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.tasks.await

class CloudAlbumPagingSource(
    private val db: FirebaseFirestore,
    private val userUID: String
) : PagingSource<QuerySnapshot, CloudAlbum>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CloudAlbum> {
        return try {

            val currentPage = params.key
                ?: db.collection("albums")
                    .whereArrayContains("members", userUID)
                    .orderBy("name", Query.Direction.ASCENDING)
                    .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                    .get()
                    .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("albums")
                .whereArrayContains("members", userUID)
                .orderBy("name", Query.Direction.ASCENDING)
                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(CloudAlbum::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            Log.d("KCH", "$e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CloudAlbum>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}