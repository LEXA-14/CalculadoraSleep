package com.example.calculadorasleep.presentation.quality

data class SleepQualityUiState(
    val rating: Int? = null,
    val selectedTags: List<String> = emptyList(),
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
