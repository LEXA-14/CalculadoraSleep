package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
){
    suspend operator fun invoke(email: String, password: String, fullName: String): Result<Boolean> {
        if (fullName.isBlank()) {
            return Result.failure(Exception("El nombre es obligatorio"))
        }
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return runCatching { repository.register(email, password, fullName) }
    }
}