package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetAlarmUseCaseTest {
    private lateinit var repository: AlarmRepository
    private lateinit var useCase: GetAlarmUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAlarmUseCase(repository)
    }

    @Test
    fun `cuando la alarma existe retorna la alarma`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0))
        coEvery { repository.getAlarm(1) } returns alarm
        val result = useCase(1)
        assertEquals(alarm, result)
    }

    @Test
    fun `cuando la alarma no existe retorna null`() = runTest {
        coEvery { repository.getAlarm(any()) } returns null
        val result = useCase(99)
        assertNull(result)
    }
}