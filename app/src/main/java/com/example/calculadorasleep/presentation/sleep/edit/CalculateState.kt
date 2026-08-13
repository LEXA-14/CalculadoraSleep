package com.example.calculadorasleep.presentation.sleep.edit

import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import com.example.calculadorasleep.domain.sleep.model.Sleep
import java.time.LocalTime

data class CalculateState(
    val mode: SleepCalculationMode = SleepCalculationMode.WAKE_UP_AT,
    val hour: Int = 7,
    val minute: Int = 30,
    val isAm: Boolean = true,
    val targetTime: LocalTime? = null,
    val options: List<SleepTimeOption> = emptyList(),
    val selectedOption: SleepTimeOption? = null,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null
)