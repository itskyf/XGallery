package com.team02.xgallery.ui.mediaincloudalbum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.source.network.FavoriteCloudMediaPagingSource
import com.team02.xgallery.data.source.network.MediaInCloudAlbumPagingSource
import com.team02.xgallery.ui.adapter.SelectionManager
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaInCloudAlbumViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    val mediaPagingFlow = Pager(
        config = PagingConfig(pageSize = AppConstants.GALLERY_PAGE_SIZE),
        pagingSourceFactory = {
            MediaInCloudAlbumPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString(),state.get<String>("albumID").toString())
        }
    ).flow.cachedIn(viewModelScope)
    val selectionManager = SelectionManager()
    fun setAlbumId(albumId: String) {
        state["albumID"] = albumId
    }
}