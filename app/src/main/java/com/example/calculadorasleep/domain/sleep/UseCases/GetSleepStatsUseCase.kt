package com.example.calculadorasleep.domain.sleep.UseCases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSleepStatsUseCase @Inject constructor(
    private val getSleepSince: GetSleepSinceUseCase
) {
     suspend operator fun invoke(dias: Int = 7): Flow<SleepStats> {

        return getSleepSince(dias).map { registros->
            if (registros.isEmpty()) return@map SleepStats.vacio()

            val duracionPromedioMin=registros.map {
                (it.despertarTiempo-it.dormirTiempo)/ 60000
            }.average().toInt()

            val ciclosPromedio=registros.map { it.ciclos }.average()
            val calidadPromedio = registros.mapNotNull { it.calidadSleep }
                .takeIf { it.isNotEmpty() }
                ?.average()

        SleepStats(
            noches = registros.size,
            duracionPromedioMin = duracionPromedioMin,
            ciclosPromedio = ciclosPromedio,
            calidadPromedio = calidadPromedio
        )
        }
    }
}