package com.example.calculadorasleep.presentation.auth.login

import android.content.Context

sealed class LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object LoginSubmit : LoginUiEvent()
    data class GoogleSignInSubmit(val context: Context) : LoginUiEvent()
    object ClearError : LoginUiEvent()
    object ClearSuccess : LoginUiEvent()

    data class ForgotPassword(val email: String) : LoginUiEvent()
}