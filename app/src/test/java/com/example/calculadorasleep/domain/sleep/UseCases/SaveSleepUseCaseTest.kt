package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SaveSleepUseCaseTest {
    private lateinit var repository: SleepRepository
    private lateinit var useCase: SaveSleepUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = SaveSleepUseCase(repository)
    }

    @Test
    fun `guarda un sleep valido y calcula los ciclos reales`() = runTest {

        val dormir =  1_700_000_000_000L
        val despertar = dormir+9 * 60 * 60 * 1000L
        val sleep = Sleep(dormirTiempo = dormir, despertarTiempo = despertar, ciclos = 0)

        val slot = slot<Sleep>()
        coEvery { repository.upsert(capture(slot)) } returns Unit

        val result = useCase(sleep)

        assertTrue(result.isSuccess)
        assertEquals(6, slot.captured.ciclos)
    }

    @Test
    fun `no guarda si los tiempos son invalidos`() = runTest {
        val sleep = Sleep(dormirTiempo = 1000L, despertarTiempo = 1000L, ciclos = 0)

        val result = useCase(sleep)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun `no guarda si la calidad es invalida`() = runTest {
        val dormir =  1_700_000_000_000L
        val despertar = dormir +8 * 60 * 60 * 1000L
        val sleep = Sleep(dormirTiempo = dormir, despertarTiempo = despertar, ciclos = 0, calidadSleep = 9)

        val result = useCase(sleep)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun `calidad nula no bloquea el guardado`() = runTest {
        val dormir =  1_700_000_000_000L
        val despertar = dormir +8 * 60 * 60 * 1000L
        val sleep = Sleep(
            dormirTiempo = dormir,
            despertarTiempo = despertar,
            ciclos = 0,
            calidadSleep = null
        )

        coEvery { repository.upsert(any()) } returns Unit

        val result = useCase(sleep)

        assertTrue(result.isSuccess)
    }

}