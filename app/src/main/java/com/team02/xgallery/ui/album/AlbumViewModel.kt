package com.team02.xgallery.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.team02.xgallery.data.entity.Media
import com.team02.xgallery.data.repository.LocalMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    mediaRepository: LocalMediaRepository,
    state: SavedStateHandle
) : ViewModel() {
    // Don't ask me wtf below
    val mediaPagingFlow = mediaRepository
        .getPagingFlow(state.get<String>("albumId")!!)
        .filterIsInstance<PagingData<Media>>() // TODO online filter
        .cachedIn(viewModelScope)
}