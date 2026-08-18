package com.example.calculadorasleep.domain.sleep.UseCases.auth

import com.example.calculadorasleep.domain.sleep.repository.AuthRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = LogoutUseCase(repository)
    }

    @Test
    fun `invoke llama al repositorio para cerrar sesion`() = runTest {
        useCase()
        coVerify { repository.logout() }
    }
}