package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import javax.inject.Inject

class DeleteSleepUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(sleep: Sleep) = repository.delete(sleep)
}