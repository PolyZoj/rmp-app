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
class EditProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun navigateToEditProfile() {
        // Логинимся и переходим в редактирование профиля
        composeTestRule.onNodeWithText("Username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Password").performTextInput("bebra!")
        composeTestRule.onNodeWithText("Log in").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("bebra52").performClick()
    }

    @Test
    fun testEditProfileScreenElements() {
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("User Profile Settings").assertExists()
        composeTestRule.onNodeWithText("Weight (kg)").assertExists()
        composeTestRule.onNodeWithText("Daily Step Goal (steps)").assertExists()
        composeTestRule.onNodeWithText("Water Intake Goal (ml)").assertExists()
        composeTestRule.onNodeWithText("Calorie Goal").assertExists()
        composeTestRule.onNodeWithText("Save").assertExists()
    }

    @Test
    fun testSaveProfileChanges() {
        // Изменяем данные
        composeTestRule.onNodeWithText("Weight (kg)").performTextInput("70")
        composeTestRule.onNodeWithText("Daily Step Goal (steps)").performTextInput("10000")

        // Сохраняем изменения
        composeTestRule.onNodeWithText("Save").performClick()

        // Проверяем сообщение об успешном сохранении
        composeTestRule.onNodeWithText("✅ User Profile Settings successfully edited").assertExists()
    }

    @Test
    fun testNavigationBack() {
        // Возвращаемся назад
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Daily Statistics").assertExists()
    }
}