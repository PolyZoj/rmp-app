package com.ifmo.rmp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginScreenElements() {
        composeTestRule.onNodeWithText("Welcome to PolyZoj community!").assertExists()
        composeTestRule.onNodeWithText("Enter your username").assertExists()
        composeTestRule.onNodeWithText("Enter your password").assertExists()
        composeTestRule.onNodeWithText("Log in").assertExists()
        composeTestRule.onNodeWithText("Don't have an account?").assertExists()
        composeTestRule.onNodeWithText("Register").assertExists()
    }

    @Test
    fun testEmptyLogin() {
        composeTestRule.onNodeWithText("Log in").performClick()
        composeTestRule.onNodeWithText("Fields cannot be empty").assertExists()
    }


    @Test
    fun testSuccessfulLogin() {
        composeTestRule.onNodeWithText("Enter your username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Enter your password").performTextInput("bebra!")

        composeTestRule.onNodeWithText("Log in").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            runCatching {
                composeTestRule.onNodeWithTag("welcome_text").fetchSemanticsNode() != null
            }.isSuccess
        }

        composeTestRule.onNodeWithTag("welcome_text").assertExists()
    }


}