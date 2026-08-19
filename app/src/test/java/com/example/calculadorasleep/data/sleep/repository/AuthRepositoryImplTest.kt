package com.example.calculadorasleep.data.sleep.repository

import androidx.credentials.CredentialManager
import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        auth = mockk(relaxed = true)
        credentialManager = mockk(relaxed = true)
        repository = AuthRepositoryImpl(auth, credentialManager)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @Test
    fun `login exitoso retorna exito`() = runTest {
        val email = "test@test.com"
        val pass = "123456"
        val task = mockk<Task<AuthResult>>()
        every { auth.signInWithEmailAndPassword(email, pass) } returns task
        coEvery { task.await() } returns mockk()
        val result = repository.login(email, pass)
        assertTrue(result)
    }

    @Test(expected = Exception::class)
    fun `login fallido lanza excepcion`() = runTest {
        val email = "test@test.com"
        val pass = "123456"
        val task = mockk<Task<AuthResult>>()
        every { auth.signInWithEmailAndPassword(email, pass) } returns task
        coEvery { task.await() } throws Exception("Auth error")
        
        repository.login(email, pass)
    }
}