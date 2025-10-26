package com.dms.dailyjoy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<LoginNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential).await()

                _uiState.value = LoginUiState.Idle
                _navigationEvents.send(LoginNavigationEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(
                    e.localizedMessage ?: "Une erreur est survenue lors de la connexion"
                )
            }
        }
    }

    fun clearError() {
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }
}
