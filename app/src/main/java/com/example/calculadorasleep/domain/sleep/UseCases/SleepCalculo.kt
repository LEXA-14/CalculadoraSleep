package com.example.calculadorasleep.domain.sleep.UseCases

import java.time.LocalTime

data class SleepTimeOption(
    val time: LocalTime,
    val ciclos: Int,
    val duracionHoras: Double,
    val esIdeal: Boolean
)

enum class SleepCalculationMode {
    WAKE_UP_AT,
    SLEEP_AT
}