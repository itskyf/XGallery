package com.team02.xgallery.ui.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.team02.xgallery.data.repository.LocalMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    mediaRepository: LocalMediaRepository,
    state: SavedStateHandle
) : ViewModel() {
    val mediaPagingFlow = mediaRepository
        .getPagingFlow(state.get<Int>("albumId")!!)
        .cachedIn(viewModelScope)
}