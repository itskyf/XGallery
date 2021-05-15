package com.team02.xgallery.data.source.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.team02.xgallery.data.entity.CloudAlbum
import com.team02.xgallery.data.entity.User
import kotlinx.coroutines.tasks.await

class CloudAlbumMemberPagingSource(
        private val db: FirebaseFirestore,
        private val albumId: String
) : PagingSource<QuerySnapshot, User>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, User> {
        return try {
            val cloudAlbum = db.document("albums/$albumId").get().await().toObject(CloudAlbum::class.java) as CloudAlbum
            val members = mutableListOf<User>()
            for (memberUID in cloudAlbum.members!!) {
                members.add(db.document("users/$memberUID").get().await().toObject(User::class.java) as User)
            }

            LoadResult.Page(
                    data = members,
                    prevKey = null,
                    nextKey = null
            )
        } catch (e: Exception) {
            Log.d("KCH", "$e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, User>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}