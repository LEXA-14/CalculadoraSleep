package com.example.calculadorasleep.presentation.sleep.edit

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
class CalculateViewModel @Inject constructor(
    private val calculateSleepTimesUseCase: CalculateSleepTimesUseCase,
    private val resolveSleepSessionMillisUseCase: ResolveSleepSessionMillisUseCase,
    private val saveSleepUseCase: SaveSleepUseCase,
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val themeState: ThemeState
) : ViewModel() {

    private val _state = MutableStateFlow(CalculateState())
    val state: StateFlow<CalculateState> = _state.asStateFlow()

    fun onEvent(event: CalculateEvent) {
        when (event) {
            is CalculateEvent.Load -> loadAlarm(event.id)
            is CalculateEvent.ModeChanged -> {
                themeState.setDarkMode(event.mode == SleepCalculationMode.SLEEP_AT)
                _state.update { it.copy(mode = event.mode, options = emptyList()) }
            }
            is CalculateEvent.HourChanged ->
                _state.update { it.copy(hour = event.hour, options = emptyList()) }
            is CalculateEvent.MinuteChanged ->
                _state.update { it.copy(minute = event.minute, options = emptyList()) }
            is CalculateEvent.PeriodChanged ->
                _state.update { it.copy(isAm = event.isAm, options = emptyList()) }
            CalculateEvent.Calculate -> onCalculate()
            is CalculateEvent.SelectOption -> onSelectOption(event.option)
            CalculateEvent.Save -> onSave()
            CalculateEvent.Delete -> onDelete()
            CalculateEvent.ClearMessage ->
                _state.update { it.copy(message = null, error = null, saved = false, deleted = false) }
        }
    }

    private fun loadAlarm(id: Int?) {
        _state.update {
            it.copy(
                saved = false,
                deleted = false,
                message = null,
                isSaving = false,
                isDeleting = false
            )
        }

        if (id == null || id <= 0) {
            _state.value = CalculateState()
            return
        }

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
                } ?: options.firstOrNull()

                _state.update {
                    it.copy(
                        alarmId = alarm.alarmId,
                        hour = hour12,
                        minute = minute,
                        isAm = isAm,
                        targetTime = targetTime,
                        options = options,
                        selectedOption = matchingOption,
                        isNew = false,
                        error = null
                    )
                }
            } else {
                _state.value = CalculateState()
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
                selectedOption = options.find { opt -> opt.esIdeal } ?: options.firstOrNull(),
                error = null
            )
        }
    }

    private fun onSelectOption(option: SleepTimeOption) {
        _state.update { it.copy(selectedOption = option) }
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
                    alarmId = if (current.isNew) 0 else current.alarmId,
                    time = kotlinxTime,
                    isEnabled = true,
                    label = "Alarma de Ciclo (${option.ciclos} ciclos)"
                )
                alarmRepository.upsert(alarmToSave)
                alarmScheduler.schedule(alarmToSave)
                _state.update { it.copy(isSaving = false, message = "Guardado") }
                kotlinx.coroutines.delay(800)
                _state.update { it.copy(saved = true) }
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
            alarmRepository.delete(current.alarmId)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}