package com.example.calculadorasleep.presentation.alarm.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.CalculateSleepTimesUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.ResolveSleepSessionMillisUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.SaveSleepUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import com.example.calculadorasleep.presentation.darkMode.ThemeState
import com.example.calculadorasleep.presentation.sleep.SleepTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmEditViewModel @Inject constructor(
    private val calculateSleepTimesUseCase: CalculateSleepTimesUseCase,
    private val resolveSleepSessionMillisUseCase: ResolveSleepSessionMillisUseCase,
    private val saveSleepUseCase: SaveSleepUseCase,
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val themeState: ThemeState
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmEditState())
    val state: StateFlow<AlarmEditState> = _state.asStateFlow()

    fun onEvent(event: AlarmEditEvent) {
        when (event) {
            is AlarmEditEvent.Load -> loadAlarm(event.id)
            is AlarmEditEvent.ModeChanged -> {
                themeState.setDarkMode(event.mode == SleepCalculationMode.SLEEP_AT)
                _state.update { it.copy(mode = event.mode, options = emptyList()) }
            }
            is AlarmEditEvent.HourChanged ->
                _state.update { it.copy(hour = event.hour, options = emptyList()) }
            is AlarmEditEvent.MinuteChanged ->
                _state.update { it.copy(minute = event.minute, options = emptyList()) }
            is AlarmEditEvent.PeriodChanged ->
                _state.update { it.copy(isAm = event.isAm, options = emptyList()) }
            AlarmEditEvent.Calculate -> onCalculate()
            is AlarmEditEvent.SelectOption -> onSelectOption(event.option)
            AlarmEditEvent.Save -> onSave()
            AlarmEditEvent.Delete -> onDelete()
            AlarmEditEvent.ClearMessage ->
                _state.update { it.copy(message = null, error = null, saved = false, deleted = false) }
        }
    }

    private fun loadAlarm(id: Int) {
        viewModelScope.launch {
            val alarm = alarmRepository.getAlarm(id)
            if (alarm != null) {
                val hour24 = alarm.time.hour
                val minute = alarm.time.minute
                val isAm = hour24 < 12
                val hour12 = when {
                    hour24 == 0 -> 12
                    hour24 > 12 -> hour24 - 12
                    else -> hour24
                }

                val targetTime = java.time.LocalTime.of(hour24, minute)
                val options = calculateSleepTimesUseCase(targetTime, _state.value.mode)
                val matchingOption = options.firstOrNull {
                    it.time.hour == hour24 && it.time.minute == minute
                }

                _state.update {
                    it.copy(
                        alarmId = alarm.alarmId,
                        hour = hour12,
                        minute = minute,
                        isAm = isAm,
                        targetTime = targetTime,
                        options = options,
                        selectedOption = matchingOption,
                        error = null
                    )
                }
            }
        }
    }

    private fun onCalculate() {
        val current = _state.value
        val targetTime = SleepTimeUtils.toLocalTime(current.hour, current.minute, current.isAm)
        val options = calculateSleepTimesUseCase(targetTime, current.mode)
        _state.update {
            it.copy(
                targetTime = targetTime,
                options = options,
                selectedOption = null,
                error = null
            )
        }
    }

    private fun onSelectOption(option: SleepTimeOption) {
        _state.update {
            val newSelection = if (it.selectedOption == option) null else option
            it.copy(selectedOption = newSelection)
        }
    }

    private fun onSave() {
        val current = _state.value
        if (current.isSaving) return
        val option = current.selectedOption ?: return
        val targetTime = current.targetTime ?: return

        val (bedTime, wakeTime) = when (current.mode) {
            SleepCalculationMode.WAKE_UP_AT -> option.time to targetTime
            SleepCalculationMode.SLEEP_AT -> targetTime to option.time
        }
        val (dormirMillis, despertarMillis) = resolveSleepSessionMillisUseCase(bedTime, wakeTime)

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            saveSleepUseCase(
                Sleep(
                    sleepId = 0,
                    dormirTiempo = dormirMillis,
                    despertarTiempo = despertarMillis,
                    ciclos = option.ciclos
                )
            ).onSuccess {
                val kotlinxTime = kotlinx.datetime.LocalTime(option.time.hour, option.time.minute)
                val alarmToSave = Alarm(
                    alarmId = current.alarmId,
                    time = kotlinxTime,
                    isEnabled = true,
                    label = "Alarma de Ciclo (${option.ciclos} ciclos)"
                )
                alarmRepository.upsert(alarmToSave)
                val scheduleResult = alarmScheduler.schedule(alarmToSave)

                scheduleResult.onSuccess {
                    _state.update { it.copy(isSaving = false, message = "Actualizado") }
                    kotlinx.coroutines.delay(800)
                    _state.update { it.copy(saved = true) }
                }.onFailure { e ->
                    _state.update { it.copy(isSaving = false, error = e.message) }
                }
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    private fun onDelete() {
        val current = _state.value
        if (current.alarmId <= 0) return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, error = null) }
            val alarmToDelete = alarmRepository.getAlarm(current.alarmId)
            alarmRepository.delete(current.alarmId)
            if (alarmToDelete != null) {
                alarmScheduler.cancel(alarmToDelete)
            }
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}
