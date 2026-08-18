package com.example.calculadorasleep.presentation.sleep.list

import com.example.calculadorasleep.domain.sleep.model.Sleep

sealed interface HistoryUiEvent {
    data object Load : HistoryUiEvent
    data object Refresh : HistoryUiEvent
    data class Delete(val sleep: Sleep) : HistoryUiEvent
    data class FiltrarPorDias(val filtro: FechaFiltro) : HistoryUiEvent
    data object ClearMessage : HistoryUiEvent

}