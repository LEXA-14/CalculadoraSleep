package com.example.calculadorasleep.presentation.auth.register

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysElementsCorrectly() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                RegisterBody(
                    state = RegisterUiState(),
                    onEvent = {},
                    onNavigateToLogin = {},
                    onGoogleSignInClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("register_name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_email").assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_password").assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_button").assertIsDisplayed()
    }

    @Test
    fun registerScreen_passwordVisibilityToggle_appearsOnlyWhenNotEmpty() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                RegisterBody(
                    state = RegisterUiState(password = ""),
                    onEvent = {},
                    onNavigateToLogin = {},
                    onGoogleSignInClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("password_visibility_toggle").assertDoesNotExist()

        composeTestRule.setContent {
            CalculadoraSleepTheme {
                RegisterBody(
                    state = RegisterUiState(password = "abc"),
                    onEvent = {},
                    onNavigateToLogin = {},
                    onGoogleSignInClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("password_visibility_toggle").assertIsDisplayed()
    }
}