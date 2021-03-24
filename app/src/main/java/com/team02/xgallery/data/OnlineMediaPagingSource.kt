package com.team02.xgallery.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.team02.xgallery.data.entity.Media
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class OnlineMediaPagingSource(db: FirebaseFirestore) : PagingSource<QuerySnapshot, Media>() {
    private val mediaRef = db.collection("users")
        .document("admin")
        .collection("media")

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Media>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Media> {
        return try {
            Timber.d("params.loadsize: ${params.loadSize}")
            val currentPage = params.key ?: mediaRef.limit(params.loadSize.toLong())
                .get().await()
            val lastDocumentSnapshot = currentPage.documents.last()

            val nextPage = mediaRef.limit(10)
                .startAfter(lastDocumentSnapshot)
                .get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Media::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}