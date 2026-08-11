package com.example.calculadorasleep.presentacion.sleep.edit

import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption

sealed interface CalculateEvent {
    data class ModeChanged(val mode: SleepCalculationMode) : CalculateEvent
    data class HourChanged(val hour: Int) : CalculateEvent
    data class MinuteChanged(val minute: Int) : CalculateEvent
    data class PeriodChanged(val isAm: Boolean) : CalculateEvent
    data object Calculate : CalculateEvent
    data class SelectOption(val option: SleepTimeOption) : CalculateEvent
    data object ClearMessage : CalculateEvent
}