package com.example.calculadorasleep.data.sleep.mapper

import com.example.calculadorasleep.data.sleep.local.alarm.AlarmEntity
import com.example.calculadorasleep.domain.sleep.model.Alarm
import kotlinx.datetime.LocalTime

fun AlarmEntity.toDomain(): Alarm = Alarm(
        alarmId = alarmId,
        time = LocalTime(hour, minute),
        isEnabled = isEnabled,
        label = label
    )

fun Alarm.toEntity(): AlarmEntity = AlarmEntity(
        alarmId = alarmId,
        hour = time.hour,
        minute = time.minute,
        isEnabled = isEnabled,
        label = label
    )