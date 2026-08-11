package com.example.calculadorasleep.presentation.auth.login

sealed class LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object LoginSubmit : LoginUiEvent()
    data class GoogleSignInSubmit(val idToken: String) : LoginUiEvent()
    object ClearError : LoginUiEvent()

    data class ForgotPassword(val email: String) : LoginUiEvent()
}