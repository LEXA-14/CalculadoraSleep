package com.example.calculadorasleep.domain.sleep.UseCases.quality

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import javax.inject.Inject


class UpsertSleepQualityUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(sleep: Sleep): Result<Int> = runCatching {
        repository.upsert(sleep)
        sleep.sleepId
    }
}
