package com.team02.xgallery.ui.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.team02.xgallery.data.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    folderRepository: FolderRepository
) : ViewModel() {
    val folderPagingFlow = folderRepository.pagingFlow.cachedIn(viewModelScope)
}