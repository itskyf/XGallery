package com.team02.xgallery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.source.network.CloudAlbumPagingSource
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
) : ViewModel() {
    val albumPagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
        pagingSourceFactory = {
            CloudAlbumPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString())
        }
    ).flow.cachedIn(viewModelScope)
}