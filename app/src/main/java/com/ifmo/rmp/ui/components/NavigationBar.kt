package com.ifmo.rmp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val items = listOf(
        Routes.HOME to R.drawable.e_home,
        Routes.ACTIVITIES to R.drawable.e_activities,
        Routes.CLUBS to R.drawable.e_clubs,
        Routes.REWARDS to R.drawable.e_rewards,
        Routes.PROFILE to R.drawable.e_profile
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        items.forEach { (route, icon) ->
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        EmojiIcon(iconResId = icon)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = route.replaceFirstChar { it.uppercase() },
                            fontFamily = LatoFont,
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
