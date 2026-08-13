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
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(CalculateState())
    val state: StateFlow<CalculateState> = _state.asStateFlow()


    fun onEvent(event: CalculateEvent) {
        when (event) {
            is CalculateEvent.ModeChanged ->
                _state.update { it.copy(mode = event.mode, options = emptyList()) }

            is CalculateEvent.HourChanged ->
                _state.update { it.copy(hour = event.hour, options = emptyList()) }

            is CalculateEvent.MinuteChanged ->
                _state.update { it.copy(minute = event.minute, options = emptyList()) }

            is CalculateEvent.PeriodChanged ->
                _state.update { it.copy(isAm = event.isAm, options = emptyList()) }

            CalculateEvent.Calculate -> onCalculate()

            is CalculateEvent.SelectOption -> onSelectOption(event.option)

            CalculateEvent.ClearMessage ->
                _state.update { it.copy(message = null, error = null) }
        }
    }

    private fun onCalculate() {
        val current = _state.value
        val targetTime = SleepTimeUtils.toLocalTime(current.hour, current.minute, current.isAm)
        val options = calculateSleepTimesUseCase(targetTime, current.mode)
        _state.update { it.copy(targetTime = targetTime, options = options, selectedOption = null) }
    }


    private fun onSelectOption(option: SleepTimeOption) {
        val current = _state.value
        val targetTime = current.targetTime ?: return

        val (bedTime, wakeTime) = when (current.mode) {
            SleepCalculationMode.WAKE_UP_AT -> option.time to targetTime
            SleepCalculationMode.SLEEP_AT -> targetTime to option.time
        }
        val (dormirMillis, despertarMillis) = resolveSleepSessionMillisUseCase(bedTime, wakeTime)

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            saveSleepUseCase(
                Sleep(
                    dormirTiempo = dormirMillis,
                    despertarTiempo = despertarMillis,
                    ciclos = option.ciclos
                )
            ).onSuccess {
                val kotlinxTime = kotlinx.datetime.LocalTime(option.time.hour, option.time.minute)

                val newAlarm = Alarm(
                    time = kotlinxTime,
                    isEnabled = true,
                    label = "Alarma de Ciclo (${option.ciclos} ciclos)"
                )

                alarmRepository.upsert(newAlarm)
                alarmScheduler.schedule(newAlarm)

                _state.update {
                    it.copy(isSaving = false, selectedOption = option, message = "Horario guardado")
                }
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message ?: "Error al guardar") }
            }
        }
    }
}