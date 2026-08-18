package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.local.alarm.AlarmEntity
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlarmRepositoryImplTest {
    private lateinit var dao: AlarmDao
    private lateinit var repository: AlarmRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = AlarmRepositoryImpl(dao)
    }

    @Test
    fun `observeAlarms mapea correctamente de entidades a dominio`() = runTest {
        val entities = listOf(
            AlarmEntity(1, 7, 0, true, "Test"),
            AlarmEntity(2, 8, 30, false, "Work")
        )
        every { dao.observeAlarms() } returns flowOf(entities)
        val result = repository.observeAlarms().first()
        assertEquals(2, result.size)
        assertEquals(7, result[0].time.hour)
        assertEquals("Work", result[1].label)
    }

    @Test
    fun `upsert llama al dao con la entidad correcta`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(7, 0), label = "Test")
        repository.upsert(alarm)
        coVerify { dao.upsertAlarm(any()) }
    }
}