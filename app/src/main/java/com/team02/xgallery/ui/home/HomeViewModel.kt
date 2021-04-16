package com.team02.xgallery.ui.home

import androidx.lifecycle.ViewModel
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val isAvailableToLogIn
        get() = userRepository.isAvailableToLogIn
}
