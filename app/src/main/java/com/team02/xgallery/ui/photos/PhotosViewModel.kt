package com.team02.xgallery.ui.photos

import androidx.lifecycle.ViewModel
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val isAvailableToLogIn
        get() = userRepository.isAvailableToLogIn
}
