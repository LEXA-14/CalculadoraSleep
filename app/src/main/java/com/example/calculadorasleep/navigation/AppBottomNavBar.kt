package com.example.calculadorasleep.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppBottomNavBar(
    currentScreen: Screen?,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calculate") },
            label = { Text("Calculate") },
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home(0)) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "History") },
            label = { Text("History") },
            selected = currentScreen is Screen.History,
            onClick = { onNavigate(Screen.History) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Alarms") },
            label = { Text("Alarms") },
            selected = currentScreen is Screen.AlarmList,
            onClick = { onNavigate(Screen.AlarmList) }
        )
    }
}