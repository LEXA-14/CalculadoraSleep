package com.example.calculadorasleep.presentation.sleep.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.DeleteSleepUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.GetSleepStatsUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.ObserveSleepHistoryUseCase
import com.example.calculadorasleep.domain.sleep.model.Sleep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val observeSleepHistoryUseCase: ObserveSleepHistoryUseCase,
    private val getSleepStatsUseCase: GetSleepStatsUseCase,
    private val deleteSleepUseCase: DeleteSleepUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState(isLoading = true))
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        loadHistory()
        loadStats()
    }

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            HistoryUiEvent.Load -> loadHistory()
            HistoryUiEvent.Refresh -> {
                loadHistory()
                loadStats()
            }
            is HistoryUiEvent.Delete -> onDelete(event.sleep)
            HistoryUiEvent.ClearMessage -> _state.update { it.copy(message = null, error = null) }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeSleepHistoryUseCase().collectLatest { registros ->
                _state.update { it.copy(isLoading = false, registros = registros) }
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getSleepStatsUseCase(dias = 7).collectLatest { stats ->
                _state.update { it.copy(stats = stats) }
            }
        }
    }

    private fun onDelete(sleep: Sleep) {
        viewModelScope.launch {
            deleteSleepUseCase(sleep)
            _state.update { it.copy(message = "Registro eliminado") }
        }
    }
}