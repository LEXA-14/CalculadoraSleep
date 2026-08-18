package com.example.calculadorasleep.presentation.quality

sealed interface SleepQualityUiEvent {
    data class RatingChanged(val rating: Int) : SleepQualityUiEvent
    data class TagToggled(val tag: String) : SleepQualityUiEvent
    data object SaveQuality : SleepQualityUiEvent
    data object Dismiss : SleepQualityUiEvent
}
