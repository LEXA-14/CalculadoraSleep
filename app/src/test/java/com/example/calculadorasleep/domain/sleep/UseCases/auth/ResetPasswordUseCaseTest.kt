package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ResetPasswordUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: ResetPasswordUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ResetPasswordUseCase(repository)
    }

    @Test
    fun `llama al repositorio para resetear contrasena`() = runTest {
        coEvery { repository.resetPassword("test@test.com") } returns true
        val result = useCase("test@test.com")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `cuando el repositorio falla retorna error capturado`() = runTest {
        coEvery { repository.resetPassword(any()) } throws Exception("Reset error")
        val result = useCase("test@test.com")
        assertTrue(result.isFailure)
    }
}