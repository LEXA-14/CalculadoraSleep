package com.example.calculadorasleep.domain.sleep.UseCases.auth

import android.content.Context
import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(context: Context): Result<Boolean> {
        return runCatching { repository.signInWithGoogle(context) }
    }
}