package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.Validation.validateCalidad
import com.example.calculadorasleep.domain.sleep.Validation.validateTiempos
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
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