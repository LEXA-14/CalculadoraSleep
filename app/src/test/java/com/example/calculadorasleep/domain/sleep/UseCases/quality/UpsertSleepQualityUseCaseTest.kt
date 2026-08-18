package com.example.calculadorasleep.domain.sleep.UseCases.quality

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpsertSleepQualityUseCaseTest {
    private lateinit var repository: SleepRepository
    private lateinit var useCase: UpsertSleepQualityUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = UpsertSleepQualityUseCase(repository)
    }

    @Test
    fun `llama al repositorio para guardar la calidad`() = runTest {
        val sleep = Sleep(sleepId = 1, dormirTiempo = 0, despertarTiempo = 0, ciclos = 5, calidadSleep = 4)
        val result = useCase(sleep)
        assertTrue(result.isSuccess)
        coVerify { repository.upsert(sleep) }
    }
}