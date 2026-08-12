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
    object AlarmList : Screen()

    @Serializable
    object Home : Screen()
    @Serializable
    object History: Screen()

}