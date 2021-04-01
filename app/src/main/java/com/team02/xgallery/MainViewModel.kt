package com.team02.xgallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.team02.xgallery.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun isAvailableToLogIn(): Boolean {
        return userRepository.isSignedIn() && userRepository.isEmailVerified()
    }
}