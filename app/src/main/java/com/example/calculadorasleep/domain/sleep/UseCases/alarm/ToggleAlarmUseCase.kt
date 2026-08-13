package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import javax.inject.Inject

class ToggleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarm: Alarm, isEnabled: Boolean) {
        val updated = alarm.copy(isEnabled = isEnabled)
        alarmRepository.upsert(updated)
        if (isEnabled) {
            alarmScheduler.schedule(updated)
        } else {
            alarmScheduler.cancel(updated)
        }
    }
}