package com.example.calculadorasleep.domain.sleep.UseCases

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalTime

class CalculateSleepTimesUseCaseTest {

    private val useCase = CalculateSleepTimesUseCase()

    @Test
    fun `WAKE_UP_AT retorna 3 opciones con 6, 5 y 4 ciclos`() {
        val target = LocalTime.of(7, 0)

        val opciones = useCase(target, SleepCalculationMode.WAKE_UP_AT)

        assertEquals(3, opciones.size)
        assertEquals(listOf(6, 5, 4), opciones.map { it.ciclos })
    }

    @Test
    fun `WAKE_UP_AT calcula la hora de dormir restando ciclos mas tiempo de conciliar el sueno`() {
        val target = LocalTime.of(7, 0)

        val opciones = useCase(target, SleepCalculationMode.WAKE_UP_AT)

        val opcionIdeal = opciones.first { it.ciclos == 5 }
        assertEquals(LocalTime.of(7, 0).minusMinutes(464), opcionIdeal.time)
        assertEquals(7.5, opcionIdeal.duracionHoras, 0.001)
    }

    @Test
    fun `SLEEP_AT calcula la hora de despertar sumando ciclos mas tiempo de conciliar el sueno`() {
        val target = LocalTime.of(23, 0)

        val opciones = useCase(target, SleepCalculationMode.SLEEP_AT)

        val opcionIdeal = opciones.first { it.ciclos == 5 }
        assertEquals(LocalTime.of(23, 0).plusMinutes(464), opcionIdeal.time)
    }

    @Test
    fun `solo la opcion de 5 ciclos se marca como ideal`() {
        val opciones = useCase(LocalTime.of(7, 0), SleepCalculationMode.WAKE_UP_AT)

        opciones.forEach {
            assertEquals(it.ciclos == 5, it.esIdeal)
        }
    }
}