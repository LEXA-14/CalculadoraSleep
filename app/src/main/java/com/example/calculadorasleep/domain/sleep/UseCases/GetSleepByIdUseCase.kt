package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import javax.inject.Inject

class GetSleepByIdUseCase @Inject constructor(
    private val repository: SleepRepository
) {
    suspend operator fun invoke(id: Int) = repository.getById(id)
}