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

    @Test
    fun testMainScreenElements() {
        composeTestRule.onNodeWithText("Enter your username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Enter your password").performTextInput("bebra!")
        composeTestRule.onNodeWithText("Log in").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("profile_button").fetchSemanticsNode() != null
            }.isSuccess
        }
        composeTestRule.onNodeWithTag("profile_button").assertExists()
    }

    @Test
    fun testMainScreenClubs() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            runCatching {
                composeTestRule.onNodeWithTag("clubs_button").fetchSemanticsNode() != null
            }.isSuccess
        }

        composeTestRule.onNodeWithTag("clubs_button").assertExists()
    }

    @Test
    fun testMainScreenAddWorkout() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("workout_button").fetchSemanticsNode() != null
            }.isSuccess
        }

        composeTestRule.onNodeWithTag("workout_button").assertExists()
    }
}

