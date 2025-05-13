package com.ifmo.rmp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("Welcome to PolyZoj community!").assertExists()
        composeTestRule.onNodeWithText("Enter your username").assertExists()
        composeTestRule.onNodeWithText("Enter your password").assertExists()
        composeTestRule.onNodeWithText("Log in").assertExists()
        composeTestRule.onNodeWithText("Don't have an account?").assertExists()
        composeTestRule.onNodeWithText("Register").assertExists()
    }

    @Test
    fun testEmptyLogin() {
        // Нажимаем кнопку без ввода данных
        composeTestRule.onNodeWithText("Log in").performClick()

        // Проверяем сообщение об ошибке
        composeTestRule.onNodeWithText("Fields cannot be empty").assertExists()
    }


    @Test
    fun testSuccessfulLogin() {
        // Вводим тестовые данные
        composeTestRule.onNodeWithText("Enter your username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Enter your password").performTextInput("bebra!")

        // Нажимаем кнопку входа
        composeTestRule.onNodeWithText("Log in").performClick()

        // Проверяем переход на главный экран
        composeTestRule.onNodeWithText("Welcome,").assertExists()
    }
}