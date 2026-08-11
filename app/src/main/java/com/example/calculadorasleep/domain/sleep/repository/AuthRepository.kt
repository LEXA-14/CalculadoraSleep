package com.example.calculadorasleep.domain.sleep.repository

import com.example.calculadorasleep.domain.sleep.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun register(email: String, password: String, fullName: String): Result<Boolean>
    suspend fun signInWithGoogle(idToken: String): Result<Boolean>

    suspend fun resetPassword(email: String): Result<Boolean>
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserName(): String?
    suspend fun logout(): Unit
}