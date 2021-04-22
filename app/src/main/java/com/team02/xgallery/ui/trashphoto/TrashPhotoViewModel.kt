package com.team02.xgallery.ui.trashphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team02.xgallery.data.repository.CloudMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashPhotoViewModel @Inject constructor(
    private val cloudMediaRepository: CloudMediaRepository
) : ViewModel() {
    private val _restoreState = MutableStateFlow(false)
    val restoreState: StateFlow<Boolean> get() = _restoreState

    fun restoreMedia(mediaId: String) {
        viewModelScope.launch {
            cloudMediaRepository.updateState(mediaId, CloudMediaRepository.StateField.DELETED, false)
            _restoreState.value = cloudMediaRepository.getState(mediaId, CloudMediaRepository.StateField.DELETED) == false
        }
    }
}