package com.example.calculadorasleep.data.sleep.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.calculadorasleep.R
import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        return true
    }

    override suspend fun register(email: String, password: String, fullName: String): Boolean {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName.trim())
            .build()

        auth.currentUser?.updateProfile(profileUpdates)?.await()
        auth.currentUser?.reload()?.await()

        return true
    }

    override suspend fun signInWithGoogle(context: Context): Boolean {
        val serverClientId = context.getString(R.string.web_client_id)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            val authCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(authCredential).await()
            return true
        } else {
            throw Exception("No valid credential found")
        }
    }

    override suspend fun resetPassword(email: String): Boolean {
        auth.sendPasswordResetEmail(email.trim()).await()
        return true
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName
    }

    override suspend fun logout() {
        auth.signOut()
        runCatching {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }
}