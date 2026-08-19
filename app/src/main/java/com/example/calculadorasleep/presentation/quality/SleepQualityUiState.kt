package com.example.calculadorasleep.presentation.quality

data class SleepQualityUiState(
    val rating: Int? = null,
    val selectedTag: String? = null,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
