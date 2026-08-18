package com.example.calculadorasleep.domain.sleep.UseCases

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class ResolveSleepSessionMillisUseCase @Inject constructor() {

    operator fun invoke(bedTime: LocalTime, wakeTime: LocalTime): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val bedDateTime = LocalDateTime.of(today, bedTime)
        val wakeDateTime = if (wakeTime <= bedTime) {
            LocalDateTime.of(today.plusDays(1), wakeTime)
        } else {
            LocalDateTime.of(today, wakeTime)
        }
        return bedDateTime.atZone(zone).toInstant().toEpochMilli() to
                wakeDateTime.atZone(zone).toInstant().toEpochMilli()
    }
}