package com.team02.xgallery.ui.auth.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState.INPUT)
    val uiState: StateFlow<RegisterState> = _uiState

    suspend fun register(displayName: String, email: String, password: String) {
        try {
            _uiState.value = RegisterState.LOADING
            userRepository.signUp(displayName, email, password)
            _uiState.value = RegisterState.SUCCESS
        } catch (e: FirebaseAuthWeakPasswordException) {
            _uiState.value = RegisterState.WEAK_PASSWORD
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _uiState.value = RegisterState.MALFORMED_EMAIL
        } catch (e: FirebaseAuthUserCollisionException) {
            _uiState.value = RegisterState.EXISTING_EMAIL
        } catch (e: Exception) {
            _uiState.value = RegisterState.ERROR
        }
    }

    fun tryAgain() {
        _uiState.value = RegisterState.INPUT
    }
}
