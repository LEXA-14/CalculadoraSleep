package com.example.calculadorasleep.presentation.quality

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SleepQualityScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun qualityScreen_displaysElements() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                SleepQualityBody(
                    state = SleepQualityUiState(),
                    onEvent = {}
                )
            }
        }
        
        composeTestRule.onNodeWithText("¿Cómo dormiste anoche?").assertIsDisplayed()
        composeTestRule.onNodeWithTag("star_rating_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("star_rating_5").assertIsDisplayed()
        composeTestRule.onNodeWithTag("save_quality_btn").assertIsDisplayed()
    }

    @Test
    fun qualityScreen_selectionLogic_works() {
        var capturedRating: Int? = null
        var capturedTag: String? = null
        
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                SleepQualityBody(
                    state = SleepQualityUiState(rating = 3, selectedTag = "Interrumpido"),
                    onEvent = { event ->
                        when (event) {
                            is SleepQualityUiEvent.RatingChanged -> capturedRating = event.rating
                            is SleepQualityUiEvent.TagToggled -> capturedTag = event.tag
                            else -> {}
                        }
                    }
                )
            }
        }
        
        composeTestRule.onNodeWithTag("star_rating_5").performClick()
        assert(capturedRating == 5)
        
        composeTestRule.onNodeWithTag("tag_Me siento descansado").performClick()
        assert(capturedTag == "Me siento descansado")
    }

    @Test
    fun qualityScreen_saveButtonDisabled_whenNoRating() {
        composeTestRule.setContent {
            CalculadoraSleepTheme {
                SleepQualityBody(
                    state = SleepQualityUiState(rating = null),
                    onEvent = {}
                )
            }
        }
        
        composeTestRule.onNodeWithTag("save_quality_btn").assertIsNotEnabled()
    }
}