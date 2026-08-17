package com.example.calculadorasleep.domain.sleep.UseCases

data class SleepStats(
    val noches: Int,
    val duracionPromedioMin: Int,
    val ciclosPromedio: Double,
    val calidadPromedio: Double?
) {
    companion object {
        fun vacio() = SleepStats(0, 0, 0.0, null)
    }
}