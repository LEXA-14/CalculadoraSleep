package com.example.calculadorasleep.presentation.sleep.edit

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
class CalculateScreenUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculateScreen_displaysModeSelectorAndCalculateButton() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                CalculateBody(state = CalculateState(), onEvent = {}, onLogout = {})
            }
        }
        composeTestRule.onNodeWithTag("chip_wake_up").assertIsDisplayed()
        composeTestRule.onNodeWithTag("chip_sleep_at").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_calculate").assertIsDisplayed()
    }

    @Test
    fun calculateScreen_showsOptionsAndSelectionWorks() {
        val options = listOf(
            SleepTimeOption(LocalTime.of(22, 30), 6, 9.0, false),
            SleepTimeOption(LocalTime.of(0, 0), 5, 7.5, true)
        )
        var capturedOption: SleepTimeOption? = null
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                CalculateBody(
                    state = CalculateState(options = options, selectedOption = null),
                    onEvent = { event ->
                        if (event is CalculateEvent.SelectOption) capturedOption = event.option
                    },
                    onLogout = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("option_card_6").assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_card_5").assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_card_6").performClick()
        assert(capturedOption?.ciclos == 6)
    }
}