package com.team02.xgallery.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.team02.xgallery.data.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    mediaRepository: MediaRepository
) : ViewModel() {
    val flow = mediaRepository.getOnlinePagingFlow().cachedIn(viewModelScope)
}
