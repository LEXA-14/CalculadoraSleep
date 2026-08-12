package com.example.calculadorasleep.presentation.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm

sealed interface AlarmUiEvent {
    data class ToggleAlarm(val alarm: Alarm, val isEnabled: Boolean) : AlarmUiEvent
    data class DeleteAlarm(val alarm: Alarm) : AlarmUiEvent
    data class SaveAlarm(val alarm: Alarm) : AlarmUiEvent
    object ClearMessage : AlarmUiEvent
}