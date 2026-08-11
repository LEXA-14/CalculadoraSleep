package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("El correo y contraseña no pueden estar vacíos"))
        }
        return repository.login(email, password)
    }
}