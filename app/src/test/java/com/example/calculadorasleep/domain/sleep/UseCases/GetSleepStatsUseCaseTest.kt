package com.example.calculadorasleep.domain.sleep.UseCases

import app.cash.turbine.test
import com.example.calculadorasleep.domain.sleep.model.Sleep
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetSleepStatsUseCaseTest {

    private lateinit var getSleepSince: GetSleepSinceUseCase
    private lateinit var useCase: GetSleepStatsUseCase

    @Before
    fun setup() {
        getSleepSince = mockk()
        useCase = GetSleepStatsUseCase(getSleepSince)
    }

    @Test
    fun `retorna estadisticas vacias cuando no hay registros`() = runTest {
        coEvery { getSleepSince(7) } returns flowOf(emptyList())

        useCase(7).test {
            val resultado = awaitItem()
            assertEquals(0, resultado.noches)
            assertEquals(0, resultado.duracionPromedioMin)
            assertEquals(0.0, resultado.ciclosPromedio, 0.001)
            assertEquals(null, resultado.calidadPromedio)
            awaitComplete()
        }
    }

    @Test
    fun `calcula el promedio de ciclos y calidad con registros validos`() = runTest {
        val registros = listOf(
            Sleep(
                dormirTiempo = 0L,
                despertarTiempo = 8 * 60 * 60 * 1000L,
                ciclos = 5,
                calidadSleep = 4
            ),
            Sleep(dormirTiempo = 0L, despertarTiempo = 6 * 60 * 60 * 1000L, ciclos = 4, calidadSleep = 2)
        )
        coEvery { getSleepSince(7) } returns flowOf(registros)

        useCase(7).test {
            val resultado = awaitItem()
            assertEquals(2, resultado.noches)
            assertEquals(4.5, resultado.ciclosPromedio, 0.001)
            assertEquals(3.0, resultado.calidadPromedio!!, 0.001)
            awaitComplete()
        }
    }

    @Test
    fun `calidadPromedio es null si ningun registro tiene calidad`() = runTest {
        val registros = listOf(
            Sleep(dormirTiempo = 0L, despertarTiempo = 8 * 60 * 60 * 1000L, ciclos = 5, calidadSleep = null)
        )
        coEvery { getSleepSince(7) } returns flowOf(registros)

        useCase(7).test {
            val resultado = awaitItem()
            assertEquals(null, resultado.calidadPromedio)
            awaitComplete()
        }
    }
}
