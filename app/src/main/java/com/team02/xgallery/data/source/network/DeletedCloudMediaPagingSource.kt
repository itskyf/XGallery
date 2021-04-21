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

class DeletedCloudMediaPagingSource(
    private val db: FirebaseFirestore,
    private val userUID: String
) : PagingSource<QuerySnapshot, CloudMedia>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CloudMedia> {
        return try {
            val currentPage = params.key
                ?: db.collection("media")
                    .whereEqualTo("owner", userUID)
                    .whereEqualTo("isDeleted", true)
                    .orderBy("dateTaken", Query.Direction.DESCENDING)
                    .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                    .get()
                    .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("media")
                .whereEqualTo("owner", userUID)
                .whereEqualTo("isDeleted", true)
                .orderBy("dateTaken", Query.Direction.DESCENDING)
                .startAfter(lastDocumentSnapshot)
                .limit(AppConstants.GALLERY_PAGE_SIZE.toLong())
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(CloudMedia::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            Log.d("KCH", "$e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CloudMedia>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}