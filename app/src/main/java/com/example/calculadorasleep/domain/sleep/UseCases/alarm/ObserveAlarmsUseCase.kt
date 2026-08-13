package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = alarmRepository.observeAlarms()
}