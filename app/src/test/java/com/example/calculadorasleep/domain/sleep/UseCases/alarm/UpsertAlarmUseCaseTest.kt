package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpsertAlarmUseCaseTest {
    private lateinit var repository: AlarmRepository
    private lateinit var useCase: UpsertAlarmUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertAlarmUseCase(repository)
    }

    @Test
    fun `cuando la etiqueta es invalida retorna error`() = runTest {
        val alarm = Alarm(time = LocalTime(7, 0), label = "")
        val result = useCase(alarm)
        assertTrue(result.isFailure)
        assertEquals("La etiqueta no puede estar vacía", result.exceptionOrNull()?.message)
    }

    @Test
    fun `cuando los datos son validos llama al repositorio`() = runTest {
        val alarm = Alarm(time = LocalTime(7, 0), label = "Test Alarm")
        coEvery { repository.upsert(alarm) } returns 1
        val result = useCase(alarm)
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
    }
}