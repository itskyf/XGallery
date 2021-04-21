package com.team02.xgallery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team02.xgallery.data.repository.CloudMediaRepository
import com.team02.xgallery.data.source.network.CloudMediaPagingSource
import com.team02.xgallery.ui.adapter.SelectionManager
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
//        cloudMediaRepository: CloudMediaRepository    // TODO
) : ViewModel() {
    // val mediaPagingFlow = cloudMediaRepository.pagingFlow.cachedIn(viewModelScope)   // TODO
    val mediaPagingFlow = Pager(
            config = PagingConfig(pageSize = AppConstants.ALBUM_PAGE_SIZE),
            pagingSourceFactory = {
                CloudMediaPagingSource(Firebase.firestore, Firebase.auth.currentUser?.uid.toString())
            }
    ).flow.cachedIn(viewModelScope)
    val selectionManager = SelectionManager()
}