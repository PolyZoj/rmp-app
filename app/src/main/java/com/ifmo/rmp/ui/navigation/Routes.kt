package com.ifmo.rmp.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ifmo.rmp.ui.screens.editProfile.EditProfileScreen
import com.ifmo.rmp.ui.screens.profile.ProfileScreen
import com.ifmo.rmp.ui.components.BottomNavigationBar
import com.ifmo.rmp.ui.components.FriendButtonState
import com.ifmo.rmp.ui.screens.mainPage.MainPageScreen
import com.ifmo.rmp.ui.screens.activities.ActivitiesScreen
import com.ifmo.rmp.ui.screens.anotherPerson.AnotherPersonScreen
import com.ifmo.rmp.ui.screens.rewards.RewardsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.PROFILE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                MainPageScreen()
            }
            composable(Routes.ACTIVITIES) {
                ActivitiesScreen()
            }
            composable(Routes.CLUBS) { /* TODO: Add screen */ }
            composable(Routes.REWARDS) {
                RewardsScreen()
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                    navController = navController
                )
            }
            composable(Routes.EDIT_PROFILE) {
                EditProfileScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.ANOTHER_PERSON) {
                AnotherPersonScreen(
                    friendState = FriendButtonState.AddFriend, // потом динамически
                    onFriendActionClick = {},
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Routes.ANOTHER_PERSON_WITH_ID,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: return@composable

                AnotherPersonScreen(
                    friendState = FriendButtonState.AddFriend,
                    onFriendActionClick = {},
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

