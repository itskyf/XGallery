package com.team02.xgallery.ui.auth.forgot

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotState.INPUT)
    val uiState: StateFlow<ForgotState> = _uiState

    suspend fun resetPassword(email: String) {
        try {
            _uiState.value = ForgotState.LOADING
            userRepository.sendPasswordResetEmail(email).await()
            _uiState.value = ForgotState.SUCCESS
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _uiState.value = ForgotState.MALFORMED_EMAIL
        } catch (e: FirebaseAuthInvalidUserException) {
            _uiState.value = ForgotState.NOT_EXISTING_EMAIL
        } catch (e: Exception) {
            _uiState.value = ForgotState.ERROR
        }
    }

    fun tryAgain() {
        _uiState.value = ForgotState.INPUT
    }
}
