package com.example.calculadorasleep.presentation.auth.Logout


import androidx.lifecycle.ViewModel
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    suspend fun logout() {
        logoutUseCase()
    }
}