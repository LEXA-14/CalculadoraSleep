package com.example.calculadorasleep.presentation.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.auth.GoogleSignInUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LoginUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LogoutUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.auth.ResetPasswordUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var googleSignInUseCase: GoogleSignInUseCase
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        loginUseCase = mockk(relaxed = true)
        googleSignInUseCase = mockk(relaxed = true)
        resetPasswordUseCase = mockk(relaxed = true)
        logoutUseCase=mockk(relaxed = true)
        viewModel = LoginViewModel(loginUseCase, googleSignInUseCase, resetPasswordUseCase,logoutUseCase)
    }

    @Test
    fun `cuando el login es exitoso el estado es isSuccess`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(true)

        viewModel.onEvent(LoginUiEvent.EmailChanged("test@test.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("123456"))
        viewModel.onEvent(LoginUiEvent.LoginSubmit)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSuccess)
    }

    @Test
    fun `cuando el login falla el estado tiene error`() = runTest {
        val errorMsg = "Credenciales incorrectas"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMsg))

        viewModel.onEvent(LoginUiEvent.LoginSubmit)
        advanceUntilIdle()

        assertEquals(errorMsg, viewModel.state.value.error)
    }

    @Test
    fun `cuando se limpia el exito el estado se reinicia totalmente`() = runTest {
        viewModel.onEvent(LoginUiEvent.EmailChanged("test@test.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("123456"))
        viewModel.onEvent(LoginUiEvent.ClearSuccess)
        advanceUntilIdle()

        assertEquals("", viewModel.state.value.email)
        assertEquals("", viewModel.state.value.password)
        assertFalse(viewModel.state.value.isSuccess)
    }
    @Test
    fun `cuando se hace logout se llama al logoutUseCase`() = runTest {
        coEvery { logoutUseCase() } just Runs

        viewModel.onEvent(LoginUiEvent.Logout)

        advanceUntilIdle()

        coVerify { logoutUseCase() }
    }

}