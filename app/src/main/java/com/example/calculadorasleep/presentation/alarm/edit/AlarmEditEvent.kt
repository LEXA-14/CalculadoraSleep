package com.example.calculadorasleep.presentation.alarm.edit

import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption

sealed interface AlarmEditEvent {
    data class Load(val id: Int) : AlarmEditEvent
    data class ModeChanged(val mode: SleepCalculationMode) : AlarmEditEvent
    data class HourChanged(val hour: Int) : AlarmEditEvent
    data class MinuteChanged(val minute: Int) : AlarmEditEvent
    data class PeriodChanged(val isAm: Boolean) : AlarmEditEvent
    data object Calculate : AlarmEditEvent
    data class SelectOption(val option: SleepTimeOption) : AlarmEditEvent
    data object Save : AlarmEditEvent
    data object Delete : AlarmEditEvent
    data object ClearMessage : AlarmEditEvent
}