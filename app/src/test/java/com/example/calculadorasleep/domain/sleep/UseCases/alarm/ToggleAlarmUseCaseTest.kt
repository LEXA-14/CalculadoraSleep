package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleAlarmUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var toggleAlarmUseCase: ToggleAlarmUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        scheduler = mockk(relaxed = true)
        toggleAlarmUseCase = ToggleAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `cuando se activa una alarma se programa en el scheduler`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = false)
        val updated = alarm.copy(isEnabled = true)

        toggleAlarmUseCase(alarm, true)

        coVerify { repository.upsert(updated) }
        coVerify { scheduler.schedule(updated) }
    }

    @Test
    fun `cuando se desactiva una alarma se cancela en el scheduler`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = true)
        val updated = alarm.copy(isEnabled = false)

        toggleAlarmUseCase(alarm, false)

        coVerify { repository.upsert(updated) }
        coVerify { scheduler.cancel(updated) }
    }

    @Test(expected = Exception::class)
    fun `cuando el repositorio falla al actualizar se propaga la excepcion`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = true)
        coEvery { repository.upsert(any()) } throws Exception("Error")

        toggleAlarmUseCase(alarm, false)
    }
}
