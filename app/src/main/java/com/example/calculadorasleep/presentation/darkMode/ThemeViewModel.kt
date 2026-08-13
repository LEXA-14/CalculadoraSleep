package com.example.calculadorasleep.presentation.darkMode

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    themeState: ThemeState
) : ViewModel() {
    val isDarkMode: StateFlow<Boolean> = themeState.isDarkMode
}