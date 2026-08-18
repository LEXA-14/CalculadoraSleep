package com.example.calculadorasleep.presentation.auth.register

import android.content.Context

sealed class RegisterUiEvent {
    data class FullNameChanged(val value: String) : RegisterUiEvent()
    data class EmailChanged(val value: String) : RegisterUiEvent()
    data class PasswordChanged(val value: String) : RegisterUiEvent()
    object RegisterSubmit  : RegisterUiEvent()
    data class GoogleSignInSubmit(val context: Context) : RegisterUiEvent()
    object ClearError : RegisterUiEvent()
}
