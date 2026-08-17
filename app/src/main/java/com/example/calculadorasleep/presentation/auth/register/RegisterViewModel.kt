package com.example.calculadorasleep.presentation.auth.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.auth.GoogleSignInUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
): ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.FullNameChanged -> _state.update { it.copy(fullName = event.value, error = null) }
            is RegisterUiEvent.EmailChanged -> _state.update { it.copy(email = event.value, error = null) }
            is RegisterUiEvent.PasswordChanged -> _state.update { it.copy(password = event.value, error = null) }
            RegisterUiEvent.RegisterSubmit -> registerWithEmail()
            is RegisterUiEvent.GoogleSignInSubmit -> loginWithGoogle(event.context)
            RegisterUiEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun registerWithEmail() {
        viewModelScope.launch {
            if (state.value.fullName.isBlank()) {
                _state.update { it.copy(error = "El nombre no puede estar vacío") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(
                email = state.value.email,
                password = state.value.password,
                fullName = state.value.fullName
            )

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