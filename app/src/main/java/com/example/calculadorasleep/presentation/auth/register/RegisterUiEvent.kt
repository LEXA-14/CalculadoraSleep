package com.example.calculadorasleep.presentation.auth.register

sealed class RegisterUiEvent {
    data class FullNameChanged(val value: String) : RegisterUiEvent()
    data class EmailChanged(val value: String) : RegisterUiEvent()
    data class PasswordChanged(val value: String) : RegisterUiEvent()
    object RegisterSubmit  : RegisterUiEvent()
    data class GoogleSignInSubmit(val idToken: String) : RegisterUiEvent()
    object ClearError : RegisterUiEvent()
}
