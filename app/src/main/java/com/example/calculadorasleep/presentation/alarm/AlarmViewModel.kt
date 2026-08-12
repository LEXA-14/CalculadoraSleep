package com.example.calculadorasleep.presentation.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
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
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
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
                    val updated = event.alarm.copy(isEnabled = event.isEnabled)
                    alarmRepository.upsert(updated)
                    if (event.isEnabled) {
                        alarmScheduler.schedule(updated)
                    } else {
                        alarmScheduler.cancel(updated)
                    }
                }
            }

            is AlarmUiEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    alarmScheduler.cancel(event.alarm)
                    alarmRepository.delete(event.alarm.alarmId)
                    _state.update { it.copy(message = "Alarma eliminada") }
                }
            }

            is AlarmUiEvent.SaveAlarm -> {
                viewModelScope.launch {
                    alarmRepository.upsert(event.alarm)

                    if (event.alarm.isEnabled) {
                        alarmScheduler.schedule(event.alarm)
                    }
                    _state.update { it.copy(message = "Alarma guardada") }
                }
            }
            AlarmUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            alarmRepository.observeAlarms().collectLatest { list ->
                _state.update { it.copy(isLoading = false, alarms = list, message = null) }
            }
        }
    }
}