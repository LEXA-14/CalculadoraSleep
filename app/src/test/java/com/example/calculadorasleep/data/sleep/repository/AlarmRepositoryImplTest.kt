package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.alarm.AlarmDao
import com.example.calculadorasleep.data.sleep.local.alarm.AlarmEntity
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: AlarmRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        auth = mockk(relaxed = true)
        val user = mockk<FirebaseUser>(relaxed = true)
        every { user.uid } returns "user1"
        every { auth.currentUser } returns user

        every { auth.addAuthStateListener(any()) } answers {
            val listener = firstArg<FirebaseAuth.AuthStateListener>()
            listener.onAuthStateChanged(auth)
        }
        repository = AlarmRepositoryImpl(dao,auth)

    }

    @Test
    fun `observeAlarms mapea correctamente de entidades a dominio`() = runTest {
        val entities = listOf(
            AlarmEntity(1, "user1", 7,0, true, "Test"),
            AlarmEntity(2, "user1", 8, 30,false, "Work")
        )
        every { dao.observeAlarms("user1") } returns flowOf(entities)
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