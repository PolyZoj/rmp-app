package com.ifmo.rmp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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

    @Before
    fun login() {
        // Предварительный вход в систему
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Log in").performClick()
    }

    @Test
    fun testMainScreenElements() {
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("Welcome,").assertExists()
        composeTestRule.onNodeWithContentDescription("Notifications").assertExists()
        composeTestRule.onNodeWithText("Profile").assertExists()
        composeTestRule.onNodeWithText("Clubs").assertExists()
        composeTestRule.onNodeWithText("Add Workout").assertExists()
        composeTestRule.onNodeWithText("Daily Goal Progress").assertExists()
        composeTestRule.onNodeWithText("Available Challenges").assertExists()
    }

    @Test
    fun testNavigationToProfile() {
        // Переходим в профиль
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Daily Statistics").assertExists()
    }

    @Test
    fun testNavigationToClubs() {
        // Переходим в клубы
        composeTestRule.onNodeWithText("Clubs").performClick()
        composeTestRule.onNodeWithText("Your current club").assertExists()
    }

    @Test
    fun testNavigationToAddActivity() {
        // Переходим в добавление активности
        composeTestRule.onNodeWithText("Add Workout").performClick()
        composeTestRule.onNodeWithText("Add your activity").assertExists()
    }
}