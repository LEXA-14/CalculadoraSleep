package com.example.calculadorasleep.presentation.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.auth.RegisterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        registerUseCase = mockk(relaxed = true)
        viewModel = RegisterViewModel(registerUseCase, mockk(relaxed = true))
    }

    @Test
    fun `registro exitoso actualiza isSuccess`() = runTest {
        coEvery { registerUseCase(any(), any(), any()) } returns Result.success(true)

        viewModel.onEvent(RegisterUiEvent.FullNameChanged("Test User"))
        viewModel.onEvent(RegisterUiEvent.EmailChanged("test@test.com"))
        viewModel.onEvent(RegisterUiEvent.PasswordChanged("123456"))
        viewModel.onEvent(RegisterUiEvent.RegisterSubmit)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSuccess)
    }

    @Test
    fun `cuando el nombre esta vacio muestra error`() = runTest {
        viewModel.onEvent(RegisterUiEvent.FullNameChanged(""))
        viewModel.onEvent(RegisterUiEvent.RegisterSubmit)
        advanceUntilIdle()

        assertEquals("El nombre no puede estar vacío", viewModel.state.value.error)
    }
}