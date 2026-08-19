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

    private val tagToRating = mapOf(
        "Me siento descansado" to 5,
        "Me costó despertar" to 2,
        "Tuve sueños locos" to 3,
        "Interrumpido" to 1
    )

    fun onEvent(event: SleepQualityUiEvent) {
        when (event) {
            is SleepQualityUiEvent.RatingChanged -> {
                _state.update { currentState ->
                    val newRating = if (currentState.rating == event.rating) null else event.rating
                    val newTag = tagToRating.entries.find { it.value == newRating }?.key
                    currentState.copy(rating = newRating, selectedTag = newTag, error = null)
                }
            }
            is SleepQualityUiEvent.TagToggled -> {
                _state.update { currentState ->
                    val isSameTag = currentState.selectedTag == event.tag
                    val newTag = if (isSameTag) null else event.tag
                    val newRating = if (isSameTag) null else tagToRating[event.tag]
                    currentState.copy(selectedTag = newTag, rating = newRating, error = null)
                }
            }
            SleepQualityUiEvent.SaveQuality -> saveQuality()
            SleepQualityUiEvent.Dismiss -> {
                _state.update { it.copy(isSaved = true) }
            }
        }
    }

    private fun saveQuality() {
        val rating = _state.value.rating ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val latestSleep = observeSleepHistoryUseCase().first().firstOrNull()
            if (latestSleep != null) {
                val updatedSleep = latestSleep.copy(calidadSleep = rating)
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
