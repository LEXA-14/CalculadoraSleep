package com.example.calculadorasleep.domain.sleep.UseCases.alarm

import com.example.calculadorasleep.domain.sleep.Validation.validateLabel
import com.example.calculadorasleep.domain.sleep.Validation.validateTime
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import javax.inject.Inject

class UpsertAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(alarm: Alarm): Result<Int> {
        val labelResult = validateLabel(alarm.label)
        if (!labelResult.isValid) {
            return Result.failure(IllegalArgumentException(labelResult.error))
        }

        val timeResult = validateTime(alarm.time.hour, alarm.time.minute)
        if (!timeResult.isValid) {
            return Result.failure(IllegalArgumentException(timeResult.error))
        }

        return runCatching { alarmRepository.upsert(alarm) }
    }
}