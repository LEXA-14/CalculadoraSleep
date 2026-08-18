package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Before
import org.junit.Test

class AlarmUseCasesTest {
    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        scheduler = mockk(relaxed = true)
    }

    @Test
    fun `DeleteAlarmUseCase llama al repositorio y al scheduler`() = runTest {
        val useCase = DeleteAlarmUseCase(repository, scheduler)
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0))
        useCase(alarm)
        coVerify { scheduler.cancel(alarm) }
        coVerify { repository.delete(1) }
    }

    @Test
    fun `ToggleAlarmUseCase actualiza el estado de la alarma`() = runTest {
        val useCase = ToggleAlarmUseCase(repository, scheduler)
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = true)
        useCase(alarm, false)
        coVerify { repository.upsert(match { !it.isEnabled }) }
    }

    @Test
    fun `ObserveAlarmsUseCase obtiene el flujo del repositorio`() = runTest {
        val useCase = ObserveAlarmsUseCase(repository)
        every { repository.observeAlarms() } returns flowOf(emptyList())
        useCase()
        coVerify { repository.observeAlarms() }
    }
}