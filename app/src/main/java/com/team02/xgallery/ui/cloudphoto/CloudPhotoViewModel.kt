package com.team02.xgallery.ui.cloudphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team02.xgallery.data.repository.CloudMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudPhotoViewModel @Inject constructor(
    private val cloudMediaRepository: CloudMediaRepository
) : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    fun initState(mediaId: String) {
        viewModelScope.launch {
            _isFavorite.value = cloudMediaRepository.getState(mediaId, CloudMediaRepository.StateField.FAVORITE)
        }
    }

    fun updateFavoriteState(mediaId: String) {
        viewModelScope.launch {
            cloudMediaRepository.updateState(mediaId, CloudMediaRepository.StateField.FAVORITE, !isFavorite.value)
            _isFavorite.value = cloudMediaRepository.getState(mediaId, CloudMediaRepository.StateField.FAVORITE)
        }
    }

    fun deleteMedia(mediaId: String) {
        viewModelScope.launch {
            cloudMediaRepository.updateState(mediaId, CloudMediaRepository.StateField.DELETED, true)
            _isDeleted.value = cloudMediaRepository.getState(mediaId, CloudMediaRepository.StateField.DELETED)
        }
    }
}