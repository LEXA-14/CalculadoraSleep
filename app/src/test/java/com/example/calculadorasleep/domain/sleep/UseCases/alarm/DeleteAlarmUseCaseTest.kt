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
class DeleteAlarmUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var deleteAlarmUseCase: DeleteAlarmUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        scheduler = mockk(relaxed = true)
        deleteAlarmUseCase = DeleteAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `cuando se elimina una alarma se llama al repositorio y al scheduler`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = true)

        deleteAlarmUseCase(alarm)

        coVerify { scheduler.cancel(alarm) }
        coVerify { repository.delete(1) }
    }

    @Test(expected = Exception::class)
    fun `cuando el repositorio falla al eliminar se propaga la excepcion`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), isEnabled = true)
        coEvery { repository.delete(any()) } throws Exception("Error base de datos")

        deleteAlarmUseCase(alarm)
    }
}
