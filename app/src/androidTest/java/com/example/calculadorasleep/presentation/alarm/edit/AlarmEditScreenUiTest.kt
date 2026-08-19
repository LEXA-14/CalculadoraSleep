package com.example.calculadorasleep.presentation.alarm.edit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class AlarmEditScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alarmEditScreen_displaysInitialValues() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmEditBody(
                    state = AlarmEditState(hour = 8, minute = 0, isAm = true),
                    onEvent = {},
                    onLogout = {}
                )
            }
        }
        
        composeTestRule.onNodeWithText("Editar Alarma").assertIsDisplayed()
        composeTestRule.onNodeWithText("08").assertIsDisplayed()
        composeTestRule.onNodeWithText("00").assertIsDisplayed()
        composeTestRule.onNodeWithText("AM").assertIsDisplayed()
    }

    @Test
    fun alarmEditScreen_showsActionButtons_whenOptionsNotEmpty() {
        val options = listOf(SleepTimeOption(LocalTime.of(22, 0), 6, 9.0, false))
        
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmEditBody(
                    state = AlarmEditState(options = options),
                    onEvent = {},
                    onLogout = {}
                )
            }
        }
        
        composeTestRule.onNodeWithText("ACTUALIZAR").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_calculate").assertIsDisplayed()
    }

    @Test
    fun alarmEditScreen_selectionLogic_works() {
        val options = listOf(SleepTimeOption(LocalTime.of(22, 0), 6, 9.0, false))
        var capturedOption: SleepTimeOption? = null
        
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                AlarmEditBody(
                    state = AlarmEditState(options = options),
                    onEvent = { 
                        if (it is AlarmEditEvent.SelectOption) capturedOption = it.option 
                    },
                    onLogout = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("option_card_6").performClick()
        assert(capturedOption?.ciclos == 6)
    }
}