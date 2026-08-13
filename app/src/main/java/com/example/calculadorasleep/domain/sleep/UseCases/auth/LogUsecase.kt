package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() {
        repository.logout()
    }
}