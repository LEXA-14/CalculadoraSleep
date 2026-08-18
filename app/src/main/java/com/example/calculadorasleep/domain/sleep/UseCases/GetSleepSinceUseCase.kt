package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Debatir cuando refrescar el promedio si cada vez que se abra pantalla o mientras se agrege un resgistro nuevo
class GetSleepSinceUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(dias: Int): Flow<List<Sleep>> {
        val desde = System.currentTimeMillis() - (dias * 24 * 60 * 60 * 1000L)
        return repository.getSince(desde)
    }
}