package com.example.calculadorasleep.presentation.sleep.list

import com.example.calculadorasleep.domain.sleep.UseCases.SleepStats
import com.example.calculadorasleep.domain.sleep.model.Sleep

data class HistoryUiState(
    val isLoading: Boolean = false,
    val registros: List<Sleep> = emptyList(),
    val stats: SleepStats = SleepStats.vacio(),
    val message: String? = null,
    val error: String? = null
)