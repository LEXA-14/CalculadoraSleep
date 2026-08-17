package com.example.calculadorasleep.domain.sleep.UseCases



import java.time.LocalTime
import javax.inject.Inject


//caso de uso que calcula
class CalculateSleepTimesUseCase @Inject constructor() {

    operator fun invoke(
        targetTime: LocalTime,
        mode: SleepCalculationMode
    ): List<SleepTimeOption> {
        val minutosParaDormirse = 14L
        val minutosPorCiclo = 90L
        val ciclosSugeridos = listOf(6, 5, 4) // el del medio es el "ideal"

        return ciclosSugeridos.map { ciclos ->
            val duracionMin = ciclos * minutosPorCiclo
            val totalMin = duracionMin + minutosParaDormirse

            val resultTime = when (mode) {
                SleepCalculationMode.WAKE_UP_AT -> targetTime.minusMinutes(totalMin)
                SleepCalculationMode.SLEEP_AT -> targetTime.plusMinutes(totalMin)
            }

            SleepTimeOption(
                time = resultTime,
                ciclos = ciclos,
                duracionHoras = duracionMin / 60.0,
                esIdeal = ciclos == 5
            )
        }
    }
}