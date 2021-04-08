package com.team02.xgallery.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.team02.xgallery.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState.INPUT)
    val uiState: StateFlow<LoginState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LoginState.LOADING
                userRepository.signInWithEmailAndPassword(email, password)
                _uiState.value = if (userRepository.isAvailableToLogIn) {
                    LoginState.SUCCESS
                } else {
                    LoginState.NOT_VERIFIED
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _uiState.value = LoginState.NOT_EXISTING_EMAIL
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _uiState.value = LoginState.WRONG_PASSWORD
            } catch (e: Exception) {
                _uiState.value = LoginState.ERROR
            }
        }
    }

    fun tryAgain() {
        _uiState.value = LoginState.INPUT
    }
}
