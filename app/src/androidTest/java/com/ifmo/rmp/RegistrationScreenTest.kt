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
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun navigateToRegistration() {
        composeTestRule.onNodeWithText("Register").performClick()
    }

    @Test
    fun testRegistrationScreenElements() {
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("Create Account").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Username").assertExists()
        composeTestRule.onNodeWithText("First Name").assertExists()
        composeTestRule.onNodeWithText("Last Name").assertExists()
        composeTestRule.onNodeWithText("Date of Birth").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Confirm Password").assertExists()
        composeTestRule.onNodeWithText("Create Account").assertExists()
    }

    @Test
    fun testEmptyRegistration() {
        // Пытаемся зарегистрироваться без данных
        composeTestRule.onNodeWithText("Create Account").performClick()
        composeTestRule.onNodeWithText("Please fill all required fields").assertExists()
    }

    @Test
    fun testSuccessfulRegistration() {
        // Заполняем форму регистрации
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("First Name").performTextInput("Test")
        composeTestRule.onNodeWithText("Last Name").performTextInput("User")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")

        // Нажимаем кнопку регистрации
        composeTestRule.onNodeWithText("Create Account").performClick()

        // Проверяем переход на главный экран
        composeTestRule.onNodeWithText("Welcome,").assertExists()
    }
}