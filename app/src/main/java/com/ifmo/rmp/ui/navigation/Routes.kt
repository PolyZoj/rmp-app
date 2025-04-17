package com.ifmo.rmp.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ifmo.rmp.ui.screens.editProfile.EditProfileScreen
import com.ifmo.rmp.ui.screens.profile.ProfileScreen
import com.ifmo.rmp.ui.components.BottomNavigationBar
import com.ifmo.rmp.ui.screens.mainPage.MainPageScreen
import com.ifmo.rmp.ui.screens.activities.ActivitiesScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.PROFILE
    ) {
        composable(Routes.HOME) {
            MainPageScreen()
        }
        composable(Routes.ACTIVITIES) {
            ActivitiesScreen()
        }
        composable(Routes.CLUBS) { /* TODO: Add screen */ }
        composable(Routes.REWARDS) { /* TODO: Add screen */ }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateToEditProfile = { navController.navigate(Routes.EDIT_PROFILE) }
            )
        }
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController)
    }
}
