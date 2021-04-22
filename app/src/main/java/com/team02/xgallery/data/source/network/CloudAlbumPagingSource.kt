package com.team02.xgallery.data.source.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.utils.AppConstants
import kotlinx.coroutines.tasks.await

class CloudAlbumPagingSource(
    private val db: FirebaseFirestore,
    private val userUID: String
) : PagingSource<QuerySnapshot, CloudAlbum>() {

    @SuppressLint("TimberArgCount")
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CloudAlbum> {
        return try {

            val currentPage = params.key
                ?: db.collection("albums")
                    .whereEqualTo("name","Study")
                    .orderBy("dateTaken", Query.Direction.DESCENDING)
                    .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                    .get()
                    .await()
            Log.d("testing","hello")
            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("albums")
                .whereEqualTo("owner", userUID)
                .orderBy("dateTaken", Query.Direction.DESCENDING)
                .startAfter(lastDocumentSnapshot)
                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(CloudAlbum::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
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