package com.example.calculadorasleep.presentation.auth.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysElementsCorrectly() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LoginBody(
                    state = LoginUiState(),
                    onEvent = {},
                    onNavigateToRegister = {},
                    onGoogleSignInClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("login_email").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_password").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun loginScreen_passwordVisibilityToggle_appearsOnlyWhenNotEmpty() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LoginBody(
                    state = LoginUiState(password = ""),
                    onEvent = {},
                    onNavigateToRegister = {},
                    onGoogleSignInClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("password_visibility_toggle").assertDoesNotExist()

        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LoginBody(
                    state = LoginUiState(password = "123"),
                    onEvent = {},
                    onNavigateToRegister = {},
                    onGoogleSignInClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("password_visibility_toggle").assertIsDisplayed()
    }

    @Test
    fun loginScreen_triggersSubmitEvent() {
        var submitCalled = false
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                LoginBody(
                    state = LoginUiState(),
                    onEvent = { if (it is LoginUiEvent.LoginSubmit) submitCalled = true },
                    onNavigateToRegister = {},
                    onGoogleSignInClick = {},
                    onForgotPasswordClick = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        assert(submitCalled)
    }
}