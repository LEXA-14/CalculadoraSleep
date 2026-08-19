package com.example.calculadorasleep.presentation.sleep.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.CalculateSleepTimesUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalTime

@ExperimentalCoroutinesApi
class CalculateViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var calculateSleepTimesUseCase: CalculateSleepTimesUseCase
    private lateinit var viewModel: CalculateViewModel

    @Before
    fun setup() {
        calculateSleepTimesUseCase = mockk(relaxed = true)
        viewModel = CalculateViewModel(
            calculateSleepTimesUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

    @Test
    fun `al calcular se obtienen las opciones y no se selecciona ninguna por defecto`() = runTest {
        val options = listOf(SleepTimeOption(LocalTime.of(23, 0), 5, 7.5, true))
        every { calculateSleepTimesUseCase(any(), any()) } returns options

        viewModel.onEvent(CalculateEvent.Calculate)
        advanceUntilIdle()

        assertEquals(options, viewModel.state.value.options)
        assertNull(viewModel.state.value.selectedOption)
    }

    @Test
    fun `seleccionar una opcion ya seleccionada la deselecciona`() {
        val option = SleepTimeOption(LocalTime.of(23, 0), 5, 7.5, true)

        viewModel.onEvent(CalculateEvent.SelectOption(option))
        assertEquals(option, viewModel.state.value.selectedOption)

        viewModel.onEvent(CalculateEvent.SelectOption(option))
        assertNull(viewModel.state.value.selectedOption)
    }

    @Test
    fun `cambiar el modo actualiza el estado y limpia opciones`() {
        viewModel.onEvent(CalculateEvent.ModeChanged(SleepCalculationMode.SLEEP_AT))

        assertEquals(SleepCalculationMode.SLEEP_AT, viewModel.state.value.mode)
        assertTrue(viewModel.state.value.options.isEmpty())
    }
}