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
class RewardsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun navigateToRewards() {
        // Логинимся и переходим в награды
        composeTestRule.onNodeWithText("Username").performTextInput("bebra52")
        composeTestRule.onNodeWithText("Password").performTextInput("bebra!")
        composeTestRule.onNodeWithText("Log in").performClick()
        composeTestRule.onNodeWithText("Rewards").performClick()
    }

    @Test
    fun testRewardsScreenElements() {
        // Проверяем наличие основных элементов
        composeTestRule.onNodeWithText("Your Achievements").assertExists()
        composeTestRule.onNodeWithText("Achievements").assertExists()
        composeTestRule.onNodeWithText("In Progress").assertExists()
    }

    @Test
    fun testAchievementsList() {
        composeTestRule.onNodeWithText("Steps").assertExists()
    }
}