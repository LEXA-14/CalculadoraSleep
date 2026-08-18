package com.example.calculadorasleep.presentation.auth.register

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.auth.GoogleSignInUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.RegisterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var googleSignInUseCase: GoogleSignInUseCase
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        registerUseCase = mockk(relaxed = true)
        googleSignInUseCase = mockk(relaxed = true)
        viewModel = RegisterViewModel(registerUseCase, googleSignInUseCase)
    }

    @Test
    fun `cuando el registro es exitoso el estado es isSuccess`() = runTest {
        val name = "Test User"
        val email = "test@test.com"
        val pass = "123456"
        coEvery { registerUseCase(email, pass, name) } returns Result.success(true)

        viewModel.onEvent(RegisterUiEvent.FullNameChanged(name))
        viewModel.onEvent(RegisterUiEvent.EmailChanged(email))
        viewModel.onEvent(RegisterUiEvent.PasswordChanged(pass))
        viewModel.onEvent(RegisterUiEvent.RegisterSubmit)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSuccess)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `cuando el nombre esta vacio no llama al use case y muestra error`() = runTest {
        viewModel.onEvent(RegisterUiEvent.FullNameChanged(""))
        viewModel.onEvent(RegisterUiEvent.RegisterSubmit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isSuccess)
        assertEquals("El nombre no puede estar vacío", viewModel.state.value.error)
    }
}