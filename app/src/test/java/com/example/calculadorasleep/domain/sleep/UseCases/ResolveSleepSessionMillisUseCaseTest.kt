package com.example.calculadorasleep.domain.sleep.UseCases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalTime

class ResolveSleepSessionMillisUseCaseTest {
    private val useCase = ResolveSleepSessionMillisUseCase()

    @Test
    fun `si wakeTime es despues de bedTime en el mismo dia`() {
        val bed = LocalTime.of(22, 0)
        val wake = LocalTime.of(23, 30)

        val (bedMillis, wakeMillis) = useCase(bed, wake)

        assertTrue(wakeMillis > bedMillis)

        assertEquals(90 * 60 * 1000L, wakeMillis - bedMillis)
    }

    @Test
    fun `si wakeTime es antes o igual a bedTime se asume que es al dia siguiente`() {
        val bed = LocalTime.of(23, 0)
        val wake = LocalTime.of(7, 0)

        val (bedMillis, wakeMillis) = useCase(bed, wake)

        assertTrue(wakeMillis > bedMillis)

        assertEquals(8 * 60 * 60 * 1000L, wakeMillis - bedMillis)
    }
}