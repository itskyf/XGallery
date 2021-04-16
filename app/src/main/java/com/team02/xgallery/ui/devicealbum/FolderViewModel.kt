package com.team02.xgallery.ui.devicealbum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.team02.xgallery.data.repository.LocalMediaRepository
import com.team02.xgallery.ui.adapter.SelectionManager
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
    val selectionManager = SelectionManager()
}