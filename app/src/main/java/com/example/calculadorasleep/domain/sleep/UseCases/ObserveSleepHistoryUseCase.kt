package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSleepHistoryUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    operator fun invoke(): Flow<List<Sleep>> = repository.getAll()
}