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

    val userName
        get() = userRepository.displayName

    val userEmail
        get() = userRepository.email

    val userAvatar
        get() = userRepository.photoUrl

    fun signOut() {
        userRepository.signOut()
    }
}
