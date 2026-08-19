package com.example.calculadorasleep.data.sleep.repository

import com.example.calculadorasleep.data.sleep.local.sleep.SleepDao
import com.example.calculadorasleep.data.sleep.local.sleep.SleepEntity
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SleepRepositoryImplTest {
    private lateinit var dao: SleepDao
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: SleepRepository

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        auth = mockk(relaxed = true)

        val user = mockk<FirebaseUser>(relaxed = true)
        every { user.uid } returns "user1"
        every { auth.currentUser } returns user

        val listenerSlot = slot<FirebaseAuth.AuthStateListener>()
        every { auth.addAuthStateListener(capture(listenerSlot)) } answers {
            listenerSlot.captured.onAuthStateChanged(auth)
        }

        repository = SleepRepositoryImpl(dao, auth)
    }

    @Test
    fun `getAll mapea correctamente de entidades a dominio`() = runTest {
        val entities = listOf(
            SleepEntity(1, "user1", 1000L, 2000L, 4, 80),
            SleepEntity(2, "user1", 3000L, 4000L, 5, 90)
        )
        every { dao.getAll("user1") } returns flowOf(entities)

        val result = repository.getAll().first()

        assertEquals(2, result.size)
        assertEquals(4, result[0].ciclos)
        assertEquals(90, result[1].calidadSleep)
    }

    @Test
    fun `getSince mapea correctamente de entidades a dominio`() = runTest {
        val entities = listOf(
            SleepEntity(1, "user1", 5000L, 6000L, 3, 70)
        )
        every { dao.getSince("user1", 1000L) } returns flowOf(entities)

        val result = repository.getSince(1000L).first()

        assertEquals(1, result.size)
        assertEquals(5000L, result[0].dormirTiempo)
    }

    @Test
    fun `getById devuelve el sleep mapeado cuando existe`() = runTest {
        val entity = SleepEntity(1, "user1", 1000L, 2000L, 4, 80)
        coEvery  { dao.getById(1) } returns entity

        val result = repository.getById(1)

        assertEquals(4, result?.ciclos)
    }
    @Test
    fun `getById devuelve null cuando no existe`() = runTest {
        coEvery { dao.getById(1) } returns null

        val result = repository.getById(1)

        assertEquals(null, result)
    }


    @Test
    fun `upsert llama al dao con la entidad correcta`() = runTest {
        val sleep = Sleep(
            sleepId = 1,
            dormirTiempo = 1000L,
            despertarTiempo = 2000L,
            ciclos = 4,
            calidadSleep = 80
        )
        repository.upsert(sleep)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete llama al dao con el id correcto`() = runTest {
        val sleep = Sleep(sleepId = 1, dormirTiempo = 1000L, despertarTiempo = 2000L, ciclos = 4)
        repository.delete(sleep)
        coVerify { dao.deleteById(1) }
    }
}

