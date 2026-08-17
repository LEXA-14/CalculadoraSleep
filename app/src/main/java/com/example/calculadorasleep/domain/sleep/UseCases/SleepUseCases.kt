package com.example.calculadorasleep.domain.sleep.UseCases

import android.os.Build

import com.example.calculadorasleep.domain.sleep.Validation.validateCalidad
import com.example.calculadorasleep.domain.sleep.Validation.validateTiempos
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class SaveSleepUseCase @Inject constructor(
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

class DeleteSleepUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(sleep: Sleep) = repository.delete(sleep)
}


//Debatir cuando refrescar el promedio si cada vez que se abra pantalla o mientras se agrege un resgistro nuevo
class GetSleepSinceUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(dias: Int): Flow<List<Sleep>> {
        val desde = System.currentTimeMillis() - (dias * 24 * 60 * 60 * 1000L)
        return repository.getSince(desde)
    }
}
class GetSleepByIdUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(id: Int) = repository.getById(id)
}

class ObserveSleepHistoryUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    operator fun invoke(): Flow<List<Sleep>> = repository.getAll()
}

class GetSleepStatsUseCase @Inject constructor(
    private val getSleepSince: GetSleepSinceUseCase
) {
     suspend operator fun invoke(dias: Int = 7): Flow<SleepStats> {

        return getSleepSince(dias).map {registros->
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

class ResolveSleepSessionMillisUseCase @Inject constructor() {

    operator fun invoke(bedTime: LocalTime, wakeTime: LocalTime): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val bedDateTime = LocalDateTime.of(today, bedTime)
        val wakeDateTime = if (wakeTime <= bedTime) {
            LocalDateTime.of(today.plusDays(1), wakeTime)
        } else {
            LocalDateTime.of(today, wakeTime)
        }
        return bedDateTime.atZone(zone).toInstant().toEpochMilli() to
                wakeDateTime.atZone(zone).toInstant().toEpochMilli()
    }
}
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