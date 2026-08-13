package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import javax.inject.Inject

class UpsertAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm): Result<Unit> {
        return runCatching {
            alarmRepository.upsert(alarm)
            if (alarm.isEnabled) {
                alarmScheduler.schedule(alarm)
            } else {
                alarmScheduler.cancel(alarm)
            }
        }
    }
}