package com.example.calculadorasleep.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object AlarmList : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class AlarmEdit(val alarmId: Int) : Screen()

    @Serializable
    data object History : Screen()

    @Serializable
    data object SleepQuality : Screen()

}