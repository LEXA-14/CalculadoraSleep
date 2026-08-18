package com.example.calculadorasleep.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.calculadorasleep.presentation.alarm.AlarmScreen
import com.example.calculadorasleep.presentation.auth.login.LoginScreen
import com.example.calculadorasleep.presentation.auth.register.RegisterScreen
import com.example.calculadorasleep.presentation.sleep.edit.CalculateScreen
import com.example.calculadorasleep.presentation.alarm.edit.AlarmEditScreen
import com.example.calculadorasleep.presentation.sleep.list.HistoryScreen
import com.example.calculadorasleep.presentation.quality.SleepQualityScreen

@Composable
fun SleepNavDisplay(isLoggedIn: Boolean = false) {
    val startScreen = if (isLoggedIn) Screen.AlarmList else Screen.Login
    val backStack = rememberNavBackStack(startScreen)
    val currentScreen = backStack.lastOrNull()
    val showBottomBar = currentScreen is Screen.Home ||
            currentScreen is Screen.AlarmEdit ||
            currentScreen is Screen.AlarmList ||
            currentScreen is Screen.History

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        if (currentScreen != screen) {
                            backStack.clear()
                            backStack.add(screen)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            entryProvider = entryProvider {
                entry<Screen.Login> {
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

                entry<Screen.Register> {
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

                entry<Screen.AlarmList> {
                    AlarmScreen(
                        onAddAlarm = {
                            backStack.add(Screen.Home)
                        },
                        onEditAlarm = { alarmId ->
                            backStack.add(Screen.AlarmEdit(alarmId))
                        },
                        onLogout = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }

                entry<Screen.Home> {
                    CalculateScreen(
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.size - 1)
                            } else {
                                backStack.clear()
                                backStack.add(Screen.AlarmList)
                            }
                        },
                        onLogout = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }

                entry<Screen.AlarmEdit> { key ->
                    AlarmEditScreen(
                        alarmId = key.alarmId,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.size - 1)
                            } else {
                                backStack.clear()
                                backStack.add(Screen.AlarmList)
                            }
                        },
                        onLogout = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }

                entry<Screen.History> {
                    HistoryScreen(
                        onLogout = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }

                entry<Screen.SleepQuality> {
                    SleepQualityScreen(
                        onNavigateBack = {
                            if (backStack.isNotEmpty()) {
                                backStack.removeAt(backStack.size - 1)
                            }
                        }
                    )
                }
            }
        )
    }
}
