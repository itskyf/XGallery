package com.team02.xgallery.ui.profile

import androidx.lifecycle.ViewModel
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val authState = userRepository.authStateFlow

    fun getDisplayName(): String {
        return userRepository.getDisplayName()
    }

    fun getEmail(): String {
        return userRepository.getEmail()
    }

    fun signOut() {
        userRepository.signOut()
    }
}
