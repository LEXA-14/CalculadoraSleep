package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: RegisterUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = RegisterUseCase(repository)
    }

    @Test
    fun `cuando el nombre esta vacio retorna error`() = runTest {
        val result = useCase("test@test.com", "123456", "")
        assertTrue(result.isFailure)
        assertEquals("El nombre es obligatorio", result.exceptionOrNull()?.message)
    }

    @Test
    fun `cuando la contrasena es corta retorna error`() = runTest {
        val result = useCase("test@test.com", "123", "Test User")
        assertTrue(result.isFailure)
        assertEquals("La contraseña debe tener al menos 6 caracteres", result.exceptionOrNull()?.message)
    }

    @Test
    fun `cuando los datos son validos retorna exito`() = runTest {
        coEvery { repository.register("test@test.com", "123456", "Test User") } returns true
        val result = useCase("test@test.com", "123456", "Test User")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `cuando el repositorio falla retorna error capturado`() = runTest {
        coEvery { repository.register(any(), any(), any()) } throws Exception("Registration error")
        val result = useCase("test@test.com", "123456", "Test User")
        assertTrue(result.isFailure)
        assertEquals("Registration error", result.exceptionOrNull()?.message)
    }
}