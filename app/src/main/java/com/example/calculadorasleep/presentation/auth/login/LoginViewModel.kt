package com.example.calculadorasleep.presentation.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.auth.GoogleSignInUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LoginUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> _state.update { it.copy(email = event.value, error = null) }
            is LoginUiEvent.PasswordChanged -> _state.update { it.copy(password = event.value, error = null) }
            LoginUiEvent.LoginSubmit -> loginWithEmail()
            is LoginUiEvent.GoogleSignInSubmit -> loginWithGoogle(event.context)
            LoginUiEvent.ClearError -> _state.update { it.copy(error = null) }
            is LoginUiEvent.ForgotPassword -> {
                viewModelScope.launch {
                    val result = resetPasswordUseCase(event.email)
                    result.onSuccess {
                        _state.value = _state.value.copy(error = "Correo de recuperación enviado. Revisa tu bandeja.")
                    }.onFailure { e ->
                        _state.value = _state.value.copy(error = e.message ?: "Error al enviar correo")
                    }
                }
            }

            LoginUiEvent.ClearSuccess -> _state.value = LoginUiState()
        }
    }

    private fun loginWithEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(state.value.email, state.value.password)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = googleSignInUseCase(context)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }
}