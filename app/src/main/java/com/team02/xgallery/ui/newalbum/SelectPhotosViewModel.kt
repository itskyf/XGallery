package com.team02.xgallery.ui.newalbum

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
class SelectPhotosViewModel @Inject constructor(
    cloudMediaRepository: CloudMediaRepository
) : ViewModel() {
    val mediaPagingFlow = cloudMediaRepository.allMediaPagingFlow.cachedIn(viewModelScope)
    val selectionManager = SelectionManager()
}