package com.example.calculadorasleep.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.calculadorasleep.presentation.alarm.AlarmScreen
import com.example.calculadorasleep.presentation.auth.login.LoginScreen
import com.example.calculadorasleep.presentation.auth.register.RegisterScreen
import com.example.calculadorasleep.presentation.sleep.edit.CalculateScreen
import com.example.calculadorasleep.presentation.sleep.list.HistoryScreen

@Composable
fun SleepNavDisplay(openDirectlyToAlarms: Boolean = false) {
    val startScreen = if (openDirectlyToAlarms) Screen.Home else Screen.Login
    val backStack = rememberNavBackStack(startScreen)

    val currentScreen=backStack.lastOrNull()
    val showBottomBar= currentScreen is Screen.Home||
            currentScreen is Screen.AlarmList
            || currentScreen is Screen.History

    Scaffold(bottomBar = {
        if (showBottomBar){
            AppBottomNavBar(
                currentScreen=currentScreen,
                onNavigate = { screen ->
                    backStack.clear()
                    backStack.add(screen)
                }
            )
        }
    }
    ) { innerPadding->
        NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding),
        entryProvider = entryProvider {
            entry<Screen.Login> { key ->
                LoginScreen(
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    },
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Screen.Home)
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
                        backStack.add(Screen.Home)
                    },


                )
            }

            entry<Screen.Home> { key ->
                CalculateScreen()
            }
            entry<Screen.History> { key->
                HistoryScreen()
            }
        }
    )
}
    }