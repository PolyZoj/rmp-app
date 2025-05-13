package com.ifmo.rmp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun navigateToProfile() {
        // Логинимся и переходим в профиль
        composeTestRule.onNodeWithText("Username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Password").performTextInput("bebra!")
        composeTestRule.onNodeWithText("Log in").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
    }

    @Test
    fun testProfileScreenElements() {
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("Daily Statistics").assertExists()
        composeTestRule.onNodeWithText("Total Steps").assertExists()
        composeTestRule.onNodeWithText("Water Intake").assertExists()
        composeTestRule.onNodeWithText("Workouts").assertExists()
        composeTestRule.onNodeWithText("Completed Challenges").assertExists()
        composeTestRule.onNodeWithText("Quick Goals").assertExists()
        composeTestRule.onNodeWithText("Find Friends").assertExists()
    }

    @Test
    fun testNavigationToEditProfile() {
        // Нажимаем на имя пользователя для перехода в редактирование
        composeTestRule.onNodeWithText("bebra52").performClick()
        composeTestRule.onNodeWithText("User Profile Settings").assertExists()
    }

    @Test
    fun testNavigationToActivities() {
        // Переходим в активности через кнопку
        composeTestRule.onNodeWithText("Daily Step Goal").performClick()
        composeTestRule.onNodeWithText("Activity Summary").assertExists()
    }
}