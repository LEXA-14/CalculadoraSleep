package com.example.calculadorasleep.presentation.alarm

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import kotlinx.datetime.LocalTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmScreenUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alarmScreen_showsEmptyMessage_whenNoAlarms() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmListBody(state = AlarmUiState(isLoading = false, alarms = emptyList()), onEvent = {}, onAddAlarm = {}, onLogout = {}, onEditAlarm = {})
            }
        }
        composeTestRule.onNodeWithTag("empty_message").assertIsDisplayed()
        composeTestRule.onNodeWithTag("fab_add").assertIsDisplayed()
    }

    @Test
    fun alarmScreen_showsAlarmList_whenAlarmsExist() {
        val alarms = listOf(Alarm(1, LocalTime(7, 0), true, "Mañana"), Alarm(2, LocalTime(8, 0), false, "Trabajo"))
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmListBody(state = AlarmUiState(isLoading = false, alarms = alarms), onEvent = {}, onAddAlarm = {}, onLogout = {}, onEditAlarm = {})
            }
        }
        composeTestRule.onNodeWithTag("alarm_item_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("alarm_item_2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("card_next_alarm").assertIsDisplayed()
    }

    @Test
    fun alarmScreen_triggersAddAlarmEvent() {
        var addAlarmCalled = false
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmListBody(state = AlarmUiState(), onEvent = {}, onAddAlarm = { addAlarmCalled = true }, onLogout = {}, onEditAlarm = {})
            }
        }
        composeTestRule.onNodeWithTag("fab_add").performClick()
        assert(addAlarmCalled)
    }
}