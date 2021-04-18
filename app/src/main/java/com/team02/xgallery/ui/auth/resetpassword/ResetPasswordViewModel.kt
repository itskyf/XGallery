package com.team02.xgallery.ui.auth.resetpassword

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordState.INPUT)
    val uiState: StateFlow<ResetPasswordState> = _uiState

    suspend fun resetPassword(email: String) {
        try {
            _uiState.value = ResetPasswordState.LOADING
            userRepository.sendPasswordResetEmail(email)
            _uiState.value = ResetPasswordState.SUCCESS
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _uiState.value = ResetPasswordState.MALFORMED_EMAIL
        } catch (e: FirebaseAuthInvalidUserException) {
            _uiState.value = ResetPasswordState.NOT_EXISTING_EMAIL
        } catch (e: Exception) {
            _uiState.value = ResetPasswordState.ERROR
        }
    }

    fun tryAgain() {
        _uiState.value = ResetPasswordState.INPUT
    }
}
