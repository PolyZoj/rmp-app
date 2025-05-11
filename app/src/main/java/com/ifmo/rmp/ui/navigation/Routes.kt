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
import com.ifmo.rmp.ui.screens.login.LoginScreen
import com.ifmo.rmp.ui.screens.login.LoginViewModel
import com.ifmo.rmp.ui.screens.mainPage.MainPageScreen
import com.ifmo.rmp.ui.screens.activities.ActivitiesScreen
import com.ifmo.rmp.ui.screens.addActivity.AddActivityScreen
import com.ifmo.rmp.ui.screens.anotherPerson.AnotherPersonScreen
import com.ifmo.rmp.ui.screens.rewards.RewardsScreen
import com.ifmo.rmp.ui.screens.clubs.ClubsScreen
import com.ifmo.rmp.ui.screens.clubs.ClubCreationScreen
import com.ifmo.rmp.ui.screens.profile.ProfileViewModel
import com.ifmo.rmp.ui.screens.registration.RegistrationScreen
import com.ifmo.rmp.ui.screens.registration.RegistrationViewModel

@Composable
fun AppNavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = LoginViewModel(),
                navController = navController
            )
        }

        composable(Routes.REGISTRATION) {
            RegistrationScreen(
                viewModel = RegistrationViewModel(),
                navController = navController
            )
        }
        
        composable(Routes.HOME) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                MainPageScreen(
                    navController = navController
                )
            }
        }
        
        composable(Routes.ACTIVITIES) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                ActivitiesScreen(
                    navController = navController
                )
            }
        }
        
        composable(Routes.CLUBS) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                ClubsScreen(navController = navController)
            }
        }
        
        composable(Routes.REWARDS) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                RewardsScreen()
            }
        }
        
        composable(Routes.PROFILE) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                    navController = navController
                )
            }
        }
        
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Routes.ANOTHER_PERSON) {
            AnotherPersonScreen(
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
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onFriendActionClick = {},
            )
        }


        composable(Routes.ADD_ACTIVITY) {
            AddActivityScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Routes.CREATE_CLUB) {
            ClubCreationScreen(
                navController = navController
            )
        }
    }
}

