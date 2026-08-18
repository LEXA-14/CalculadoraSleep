package com.example.calculadorasleep.presentation.alarm

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.DeleteAlarmUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.ObserveAlarmsUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.ToggleAlarmUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.UpsertAlarmUseCase
import com.example.calculadorasleep.domain.sleep.model.Alarm
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.datetime.LocalTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var observeAlarmsUseCase: ObserveAlarmsUseCase
    private lateinit var toggleAlarmUseCase: ToggleAlarmUseCase
    private lateinit var upsertAlarmUseCase: UpsertAlarmUseCase
    private lateinit var deleteAlarmUseCase: DeleteAlarmUseCase
    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup() {
        observeAlarmsUseCase = mockk(relaxed = true)
        toggleAlarmUseCase = mockk(relaxed = true)
        upsertAlarmUseCase = mockk(relaxed = true)
        deleteAlarmUseCase = mockk(relaxed = true)
        every { observeAlarmsUseCase() } returns flowOf(emptyList())
        viewModel = AlarmViewModel(observeAlarmsUseCase, toggleAlarmUseCase, upsertAlarmUseCase, deleteAlarmUseCase)
    }

    @Test
    fun `al iniciar carga la lista de alarmas`() = runTest {
        val alarms = listOf(
            Alarm(1, LocalTime(7, 0), true),
            Alarm(2, LocalTime(8, 30), false)
        )
        every { observeAlarmsUseCase() } returns flowOf(alarms)
        viewModel = AlarmViewModel(observeAlarmsUseCase, toggleAlarmUseCase, upsertAlarmUseCase, deleteAlarmUseCase)
        advanceUntilIdle()

        assertEquals(alarms, viewModel.state.value.alarms)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `cuando se elimina una alarma se llama al caso de uso y muestra mensaje`() = runTest {
        val alarm = Alarm(1, LocalTime(7, 0), true)
        coEvery { deleteAlarmUseCase(alarm) } returns Unit

        viewModel.onEvent(AlarmUiEvent.DeleteAlarm(alarm))
        advanceUntilIdle()

        coVerify { deleteAlarmUseCase(alarm) }
        assertEquals("Alarma eliminada", viewModel.state.value.message)
    }

    @Test
    fun `cuando se cambia el estado de una alarma se llama al caso de uso`() = runTest {
        val alarm = Alarm(1, LocalTime(7, 0), true)
        coEvery { toggleAlarmUseCase(alarm, false) } returns Unit

        viewModel.onEvent(AlarmUiEvent.ToggleAlarm(alarm, false))
        advanceUntilIdle()

        coVerify { toggleAlarmUseCase(alarm, false) }
    }
}