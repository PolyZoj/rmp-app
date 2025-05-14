package com.ifmo.rmp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun loginToApp() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithText("Enter your username").fetchSemanticsNode() != null
            }.isSuccess
        }
        
        composeTestRule.onNodeWithText("Enter your username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Enter your password").performTextInput("bebra!")
        composeTestRule.onNodeWithText("Log in").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("profile_button").fetchSemanticsNode() != null
            }.isSuccess
        }
    }

    @Test
    fun testNavigationBetweenScreens() {
        loginToApp()
        // Navigate to Profile screen
        composeTestRule.onNodeWithTag("profile_button").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithText("Daily Statistics").fetchSemanticsNode() != null
            }.isSuccess
        }
        
        // Navigate back to Main screen
        composeTestRule.onNodeWithText("Home").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("welcome_text").fetchSemanticsNode() != null
            }.isSuccess
        }
        
        // Navigate to Clubs screen
        composeTestRule.onNodeWithTag("clubs_button").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithText("Your current club").fetchSemanticsNode() != null
            }.isSuccess
        }
        
        // Navigate back to Main screen
        composeTestRule.onNodeWithText("Home").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("welcome_text").fetchSemanticsNode() != null
            }.isSuccess
        }
    }

    @Test
    fun testAddActivityWorkflow() {
        // Navigate to Add Activity screen
        composeTestRule.onNodeWithText("Add Workout").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithText("Add your activity").fetchSemanticsNode() != null
            }.isSuccess
        }
        
        // Interact with water intake controls
        composeTestRule.onNodeWithContentDescription("Increase").performClick()
        composeTestRule.onNodeWithContentDescription("Increase").performClick()
        
        // Add water
        composeTestRule.onNodeWithText("Add water").performClick()
        
        // Add workout data
        composeTestRule.onNodeWithText("Medium").performClick()
        composeTestRule.onNodeWithText("Write time in minutes").performTextInput("30")
        composeTestRule.onNodeWithText("Add training").performClick()
        
        // Return to previous screen
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("profile_button").fetchSemanticsNode() != null
            }.isSuccess
        }
    }

    @Test
    fun testClubsScreenInteraction() {
        // Navigate to Clubs screen
        composeTestRule.onNodeWithTag("clubs_button").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithText("Your current club").fetchSemanticsNode() != null
            }.isSuccess
        }

        // Navigate back to Main screen
        try {
            composeTestRule.onNodeWithText("Home").performClick()
            
            composeTestRule.waitUntil(timeoutMillis = 5000) {
                runCatching {
                    composeTestRule.onNodeWithTag("profile_button").fetchSemanticsNode() != null
                }.isSuccess
            }
        } catch (e: Exception) {
            // Try using back button if Home button doesn't exist
            composeTestRule.onNodeWithContentDescription("Back").performClick()
        }
    }

}

