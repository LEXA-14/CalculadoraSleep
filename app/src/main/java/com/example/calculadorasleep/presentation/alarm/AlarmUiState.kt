package com.example.calculadorasleep.presentation.alarm

import com.example.calculadorasleep.domain.sleep.model.Alarm

data class AlarmUiState(
    val alarms: List<Alarm> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)
