package com.example.calculadorasleep.presentation.quality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadorasleep.domain.sleep.UseCases.ObserveSleepHistoryUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.quality.UpsertSleepQualityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepQualityViewModel @Inject constructor(
    private val upsertSleepQualityUseCase: UpsertSleepQualityUseCase,
    private val observeSleepHistoryUseCase: ObserveSleepHistoryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SleepQualityUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: SleepQualityUiEvent) {
        when (event) {
            is SleepQualityUiEvent.RatingChanged -> {
                _state.update { it.copy(rating = event.rating, error = null) }
            }
            is SleepQualityUiEvent.TagToggled -> {
                _state.update { state ->
                    val newTags = if (state.selectedTags.contains(event.tag)) {
                        state.selectedTags - event.tag
                    } else {
                        state.selectedTags + event.tag
                    }
                    state.copy(selectedTags = newTags)
                }
            }
            SleepQualityUiEvent.SaveQuality -> saveQuality()
            SleepQualityUiEvent.Dismiss -> {
                _state.update { it.copy(isSaved = true) }
            }
        }
    }

    private fun saveQuality() {
        if (_state.value.rating == 0) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val latestSleep = observeSleepHistoryUseCase().first().firstOrNull()

            if (latestSleep != null) {
                val updatedSleep = latestSleep.copy(calidadSleep = _state.value.rating)
                upsertSleepQualityUseCase(updatedSleep)
                    .onSuccess {
                        _state.update { it.copy(isLoading = false, isSaved = true) }
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isLoading = false, error = error.message) }
                    }
            } else {
                _state.update { it.copy(isLoading = false, error = "No se encontró registro de sueño") }
            }
        }
    }
}
