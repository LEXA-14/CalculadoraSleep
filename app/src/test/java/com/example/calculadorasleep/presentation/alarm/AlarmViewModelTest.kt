package com.example.calculadorasleep.presentation.alarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.DeleteAlarmUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.ObserveAlarmsUseCase
import com.example.calculadorasleep.domain.sleep.model.Alarm
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var observeAlarmsUseCase: ObserveAlarmsUseCase
    private lateinit var deleteAlarmUseCase: DeleteAlarmUseCase
    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup() {
        observeAlarmsUseCase = mockk(relaxed = true)
        deleteAlarmUseCase = mockk(relaxed = true)
        every { observeAlarmsUseCase() } returns flowOf(emptyList())
        viewModel = AlarmViewModel(
            observeAlarmsUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true),
            deleteAlarmUseCase
        )
    }

    @Test
    fun `carga de alarmas exitosa al iniciar`() = runTest {
        val alarms = listOf(Alarm(1, LocalTime(7, 0), true))
        every { observeAlarmsUseCase() } returns flowOf(alarms)
        
        viewModel = AlarmViewModel(
            observeAlarmsUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true),
            deleteAlarmUseCase
        )
        advanceUntilIdle()

        assertEquals(alarms, viewModel.state.value.alarms)
    }

    @Test
    fun `eliminar alarma llama al caso de uso`() = runTest {
        val alarm = Alarm(1, LocalTime(7, 0), true)
        coEvery { deleteAlarmUseCase(alarm) } returns Unit

        viewModel.onEvent(AlarmUiEvent.DeleteAlarm(alarm))
        advanceUntilIdle()

        coVerify { deleteAlarmUseCase(alarm) }
        assertEquals("Alarma eliminada", viewModel.state.value.message)
    }
}