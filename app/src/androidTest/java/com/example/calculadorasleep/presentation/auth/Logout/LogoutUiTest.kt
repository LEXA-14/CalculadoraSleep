package com.example.calculadorasleep.presentation.auth.Logout

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogoutUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun logoutButton_opensDialog_whenClicked() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LogoutButton(onLogout = {})
            }
        }
        composeTestRule.onNodeWithTag("logout_button").performClick()
        composeTestRule.onNodeWithTag("logout_dialog").assertIsDisplayed()
    }

    @Test
    fun logoutDialog_handlesDismiss_correctly() {
        var dismissCalled = false
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LogoutDialog(onDismiss = { dismissCalled = true }, onConfirm = {})
            }
        }
        composeTestRule.onNodeWithTag("logout_cancel").performClick()
        assert(dismissCalled)
    }

    @Test
    fun logoutDialog_handlesConfirm_correctly() {
        var confirmCalled = false
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LogoutDialog(onDismiss = {}, onConfirm = { confirmCalled = true })
            }
        }
        composeTestRule.onNodeWithTag("logout_confirm").performClick()
        assert(confirmCalled)
    }
}