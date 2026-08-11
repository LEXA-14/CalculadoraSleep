package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<Boolean> {
        if (idToken.isBlank()) {
            return Result.failure(Exception("Token de Google inválido"))
        }
        return repository.signInWithGoogle(idToken)
    }
}