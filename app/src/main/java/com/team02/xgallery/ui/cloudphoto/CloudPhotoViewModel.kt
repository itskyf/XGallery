package com.team02.xgallery.ui.cloudphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team02.xgallery.data.repository.CloudPhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudPhotoViewModel @Inject constructor(
    private val cloudPhotoRepository: CloudPhotoRepository
) : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    fun initState(mediaId: String) {
        viewModelScope.launch {
            _isFavorite.value = cloudPhotoRepository.getFavoriteState(mediaId)
        }
    }

    fun updateFavoriteState(mediaId: String) {
        viewModelScope.launch {
            cloudPhotoRepository.updateFavoriteState(mediaId, !isFavorite.value)
            _isFavorite.value = cloudPhotoRepository.getFavoriteState(mediaId)
        }
    }
}