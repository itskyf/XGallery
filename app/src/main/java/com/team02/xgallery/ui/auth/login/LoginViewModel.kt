package com.team02.xgallery.ui.auth.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.team02.xgallery.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginState>(LoginState.INPUT)
    val uiState: StateFlow<LoginState> = _uiState

    suspend fun signIn(email: String, password: String) {
        try {
            _uiState.value = LoginState.LOADING
            userRepository.signInWithEmailAndPassword(email, password).await()
            if (userRepository.isEmailVerified()) {
                _uiState.value = LoginState.SUCCESS
            } else {
                _uiState.value = LoginState.NOT_VERIFIED
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            _uiState.value = LoginState.NOT_EXISTING_EMAIL
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _uiState.value = LoginState.WRONG_PASSWORD
        } catch (e: Exception) {
            Timber.d(e.localizedMessage)
            _uiState.value = LoginState.ERROR
        }
    }

    fun tryAgain() {
        _uiState.value = LoginState.INPUT
    }
}
