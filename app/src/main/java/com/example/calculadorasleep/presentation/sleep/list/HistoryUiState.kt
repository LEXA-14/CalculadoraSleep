package com.example.calculadorasleep.presentation.sleep.list

import com.example.calculadorasleep.domain.sleep.UseCases.SleepStats
import com.example.calculadorasleep.domain.sleep.model.Sleep

data class HistoryUiState(
    val isLoading: Boolean = false,
    val registros: List<Sleep> = emptyList(),
    val stats: SleepStats = SleepStats.vacio(),
    val message: String? = null,
    val error: String? = null,
    val filtroFecha: FechaFiltro= FechaFiltro.SIETE_DIAS

)

enum class FechaFiltro(val days: Int) {
    SIETE_DIAS(7),
    QUINCE_DIAS(15),
    TREINTA_DIAS(30)
}