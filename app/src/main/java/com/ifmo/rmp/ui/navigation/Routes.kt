package com.ifmo.rmp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ifmo.rmp.ui.screens.editProfile.EditProfileScreen
import com.ifmo.rmp.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.PROFILE
    ) {
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
}