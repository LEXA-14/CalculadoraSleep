package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: LoginUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `cuando email o password estan vacios retorna error`() = runTest {
        val result = useCase("", "")
        assertTrue(result.isFailure)
        assertEquals("El correo y contraseña no pueden estar vacíos", result.exceptionOrNull()?.message)
    }

    @Test
    fun `cuando email y password son correctos retorna exito`() = runTest {
        coEvery { repository.login("test@test.com", "123456") } returns true
        val result = useCase("test@test.com", "123456")
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() == true)
    }

    @Test
    fun `cuando el repositorio falla retorna error capturado`() = runTest {
        coEvery { repository.login(any(), any()) } throws Exception("Database error")
        val result = useCase("test@test.com", "123456")
        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}