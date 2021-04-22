package com.team02.xgallery.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.team02.xgallery.data.repository.CloudAlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    cloudAlbumRepository: CloudAlbumRepository
) : ViewModel() {
    val albumPagingFlow = cloudAlbumRepository.pagingFlow.cachedIn(viewModelScope)
}