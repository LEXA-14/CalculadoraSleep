package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Int): Alarm? = repository.getAlarm(id)
}