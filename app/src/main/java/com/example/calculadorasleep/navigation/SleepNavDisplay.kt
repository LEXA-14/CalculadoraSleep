package com.example.calculadorasleep.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.calculadorasleep.presentacion.sleep.edit.CalculateScreen
import com.example.calculadorasleep.presentation.alarm.AlarmScreen
import com.example.calculadorasleep.presentation.auth.login.LoginScreen
import com.example.calculadorasleep.presentation.auth.register.RegisterScreen
import com.example.calculadorasleep.presentation.sleep.edit.CalculateScreen

@Composable
fun SleepNavDisplay(openDirectlyToAlarms: Boolean = false) {
    val startScreen = if (openDirectlyToAlarms) Screen.AlarmList else Screen.Login
    val backStack = rememberNavBackStack(startScreen)

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<Screen.Login> { key ->
                LoginScreen(
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    },
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Screen.AlarmList)
                    }
                )
            }

            entry<Screen.Register> { key ->
                RegisterScreen(
                    onNavigateToLogin = {
                        if (backStack.isNotEmpty()) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(Screen.AlarmList)
                    }
                )
            }

            entry<Screen.AlarmList> { key ->
                AlarmScreen(
                    onAddAlarm = {
                        backStack.add(Screen.Calculate)
                    },
                    onNavigate = { route ->
                        when (route) {
                            is Screen.Calculate -> {
                                backStack.clear()
                                backStack.add(Screen.Calculate)
                            }
                            is Screen.AlarmList -> {}
                            else -> {}
                        }
                    }
                )
            }

            entry<Screen.Calculate> { key ->
                CalculateScreen(
//                   onNavigate = { route ->
//                        when (route) {
//                            is Screen.AlarmList -> {
//                                backStack.clear()
//                              backStack.add(Screen.AlarmList)
//                             }
//                            else -> {}
//                       }
//                   }
                )
            }
        }
    )
}