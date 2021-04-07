package com.team02.xgallery

import androidx.lifecycle.ViewModel
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val authStateFlow = userRepository.authStateFlow
   
    val isAvailableToLogIn
        get() = userRepository.isAvailableToLogIn
}