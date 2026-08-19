package com.example.calculadorasleep.domain.sleep.repository

import com.example.calculadorasleep.domain.sleep.model.User

import android.content.Context

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(email: String, password: String, fullName: String): Boolean
    suspend fun signInWithGoogle(context: Context): Boolean

    suspend fun resetPassword(email: String): Boolean
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserName(): String?
    suspend fun logout(): Unit
}