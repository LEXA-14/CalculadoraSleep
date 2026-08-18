package com.example.calculadorasleep.domain.sleep.UseCases.auth

import android.content.Context
import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GoogleSignInUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: GoogleSignInUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GoogleSignInUseCase(repository)
    }

    @Test
    fun `llama al repositorio para iniciar sesion con google`() = runTest {
        val context = mockk<Context>()
        coEvery { repository.signInWithGoogle(context) } returns Result.success(true)
        val result = useCase(context)
        assertTrue(result.isSuccess)
    }
}