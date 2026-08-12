package com.example.calculadorasleep.domain.sleep.model

import kotlinx.datetime.LocalTime

data class Alarm(
    val alarmId: Int = 0,
    val time: LocalTime,
    val isEnabled: Boolean = true,
    val label: String = "Alarma de Sueño"
)
