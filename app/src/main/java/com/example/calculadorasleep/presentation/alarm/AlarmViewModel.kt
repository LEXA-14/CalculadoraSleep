package com.example.calculadorasleep.presentation.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.DeleteAlarmUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.ObserveAlarmsUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.ToggleAlarmUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.alarm.UpsertAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val observeAlarmsUseCase: ObserveAlarmsUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase,
    private val upsertAlarmUseCase: UpsertAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmUiState(isLoading = true))
    val state: StateFlow<AlarmUiState> = _state.asStateFlow()

    init {
        loadAlarms()
    }

    fun onEvent(event: AlarmUiEvent) {
        when (event) {
            is AlarmUiEvent.ToggleAlarm -> {
                viewModelScope.launch {
                    toggleAlarmUseCase(event.alarm, event.isEnabled)
                }
            }

            is AlarmUiEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    deleteAlarmUseCase(event.alarm)
                    _state.update { it.copy(message = "Alarma eliminada") }
                }
            }

            is AlarmUiEvent.SaveAlarm -> {
                viewModelScope.launch {
                    upsertAlarmUseCase(event.alarm)
                    _state.update { it.copy(message = "Alarma guardada") }
                }
            }
            AlarmUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeAlarmsUseCase().collectLatest { list ->
                _state.update { it.copy(isLoading = false, alarms = list, message = null) }
            }
        }
    }
}