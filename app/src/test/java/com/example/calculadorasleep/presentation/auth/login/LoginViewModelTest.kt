package com.example.calculadorasleep.presentation.auth.login

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.auth.GoogleSignInUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LoginUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.ResetPasswordUseCase
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
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var googleSignInUseCase: GoogleSignInUseCase
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        loginUseCase = mockk(relaxed = true)
        googleSignInUseCase = mockk(relaxed = true)
        resetPasswordUseCase = mockk(relaxed = true)
        viewModel = LoginViewModel(loginUseCase, googleSignInUseCase, resetPasswordUseCase)
    }

    @Test
    fun `cuando el login es exitoso el estado es isSuccess`() = runTest {
        val email = "test@test.com"
        val pass = "123456"
        coEvery { loginUseCase(email, pass) } returns Result.success(true)

        viewModel.onEvent(LoginUiEvent.EmailChanged(email))
        viewModel.onEvent(LoginUiEvent.PasswordChanged(pass))
        viewModel.onEvent(LoginUiEvent.LoginSubmit)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSuccess)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `cuando el login falla el estado tiene error`() = runTest {
        val email = "test@test.com"
        val pass = "wrong"
        val errorMsg = "Credenciales incorrectas"
        coEvery { loginUseCase(email, pass) } returns Result.failure(Exception(errorMsg))

        viewModel.onEvent(LoginUiEvent.EmailChanged(email))
        viewModel.onEvent(LoginUiEvent.PasswordChanged(pass))
        viewModel.onEvent(LoginUiEvent.LoginSubmit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isSuccess)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(errorMsg, viewModel.state.value.error)
    }

    @Test
    fun `cuando se cambia el email se limpia el error`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception("Error"))
        viewModel.onEvent(LoginUiEvent.LoginSubmit)
        advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        viewModel.onEvent(LoginUiEvent.EmailChanged("nuevo@email.com"))

        assertNull(viewModel.state.value.error)
        assertEquals("nuevo@email.com", viewModel.state.value.email)
    }
}