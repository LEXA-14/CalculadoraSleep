package com.example.calculadorasleep.presentation.alarm.edit

import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import java.time.LocalTime

data class AlarmEditState(
    val alarmId: Int = 0,
    val mode: SleepCalculationMode = SleepCalculationMode.WAKE_UP_AT,
    val hour: Int = 7,
    val minute: Int = 0,
    val isAm: Boolean = true,
    val targetTime: LocalTime? = null,
    val options: List<SleepTimeOption> = emptyList(),
    val selectedOption: SleepTimeOption? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
