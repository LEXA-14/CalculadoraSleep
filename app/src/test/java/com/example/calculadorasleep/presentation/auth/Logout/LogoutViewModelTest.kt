package com.example.calculadorasleep.presentation.auth.Logout

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.auth.LogoutUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LogoutViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: LogoutViewModel

    @Before
    fun setup() {
        logoutUseCase = mockk(relaxed = true)
        viewModel = LogoutViewModel(logoutUseCase)
    }

    @Test
    fun `logout llama al caso de uso correspondiente`() = runTest {
        viewModel.logout()
        coVerify { logoutUseCase() }
    }
}