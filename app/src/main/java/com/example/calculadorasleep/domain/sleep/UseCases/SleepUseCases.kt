package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.Validation.validateCalidad
import com.example.calculadorasleep.domain.sleep.Validation.validateTiempos
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SaveSleepUseCase(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(sleep: Sleep): Result<Unit> {
        val tiemposResult = validateTiempos(sleep.dormirTiempo, sleep.despertarTiempo)
        if (!tiemposResult.isValid) {
            return Result.failure(IllegalArgumentException(tiemposResult.error))
        }

        val calidadResult = validateCalidad(sleep.calidadSleep)
        if (!calidadResult.isValid) {
            return Result.failure(IllegalArgumentException(calidadResult.error))
        }

        val duracionMinutos = (sleep.despertarTiempo - sleep.dormirTiempo) / 60000
        val ciclosReales = (duracionMinutos / 90).toInt()

        return runCatching {
            repository.upsert(sleep.copy(ciclos = ciclosReales))
        }
    }
}

class DeleteSleepUseCase(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(sleep: Sleep) = repository.delete(sleep)
}


//Debatir cuando refrescar el promedio si cada vez que se abra pantalla o mientras se agrege un resgistro nuevo
class GetSleepSinceUseCase(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(dias: Int): Flow<List<Sleep>> {
        val desde = System.currentTimeMillis() - (dias * 24 * 60 * 60 * 1000L)
        return repository.getSince(desde)
    }
}
class GetSleepByIdUseCase(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(id: Int) = repository.getById(id)
}

class ObserveSleepHistoryUseCase(
    private val repository: SleepRepository
) {
    operator fun invoke(): Flow<List<Sleep>> = repository.getAll()
}

class GetSleepStatsUseCase(
    private val getSleepSince: GetSleepSinceUseCase
) {
     suspend operator fun invoke(dias: Int = 7): Flow<SleepStats> {

        return getSleepSince(dias).map {registros->
            if (registros.isEmpty()) return@map SleepStats.vacio()

            val duracionPromedioMin=registros.map {
                (it.despertarTiempo-it.dormirTiempo)/ 6000
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