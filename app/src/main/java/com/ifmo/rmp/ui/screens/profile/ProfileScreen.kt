package com.ifmo.rmp.ui.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifmo.rmp.R
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.*
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    navController: NavController,
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val userId = sharedPreferences.getString("user_id", "") ?: ""
    val userRepository = remember { UserRepository.getInstance(context) }
    val viewModel = remember { ProfileViewModel(userRepository) }

    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    val user by viewModel.user.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(sharedPreferences) {
        viewModel.loadUser(userId)
        viewModel.loadFriends()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 12.dp)
                .padding(top = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val userAvatarResId = remember(user?.avatar_url) {
                        val name = user?.avatar_url ?: "e_profile"
                        context.resources.getIdentifier(name, "drawable", context.packageName)
                    }
                    EmojiIcon(iconResId = userAvatarResId)
                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.clickable { onNavigateToEditProfile() }) {
                        Text(
                            text = user?.username ?: "Unknown",
                            fontSize = 20.sp,
                            fontFamily = LatoFont
                        )
                        Text(
                            text = "Level 5 | 5252 XP",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = LatoFont
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Currently a member of:",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = LatoFont
                        )
                        Text(
                            text = user?.club_id.toString() ?: "No club",
                            fontSize = 16.sp,
                            fontFamily = LatoFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Weak statistics", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock(title = "Total Steps", value = "15,000", percentage = 5, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(title = "Water Intake", value = "8 cups", percentage = -10, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(title = "Workouts", value = "20", percentage = 15, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Completed Challenges", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(8.dp))

                val challenges = listOf(
                    Triple("Daily Challenge", "30-day streak", R.drawable.e_trophy),
                    Triple("Weekly Push", "5 completed", R.drawable.e_trophy),
                    Triple("Monthly Beast", "100% tasks", R.drawable.e_trophy),
                    Triple("Steps Hero", "50k steps", R.drawable.e_step),
                    Triple("Hydration King", "7-day streak", R.drawable.e_water)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(challenges.take(5)) { challenge ->
                            EmojiAndTextWithDescriptionLine(
                                iconResId = challenge.third,
                                title = challenge.first,
                                subtitle = challenge.second,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Quick Goals", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GoalBox(
                        iconResId = R.drawable.e_step,
                        text = "Daily Step Goal",
                        onClick = {
                            navController.navigate(Routes.ACTIVITIES) {
                                popUpTo(Routes.PROFILE) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    GoalBox(
                        iconResId = R.drawable.e_water,
                        text = "Water Intake",
                        onClick = {
                            navController.navigate(Routes.ACTIVITIES) {
                                popUpTo(Routes.PROFILE) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Find Friends", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(4.dp))

                val displayedFriends = if (searchQuery.isEmpty()) friends ?: emptyList() else searchResults ?: emptyList()

                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        viewModel.searchFriends(it)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 42.dp)
                    ) {
                        items(displayedFriends) { friend ->
                            val avatarResId = remember(friend.avatar_url) {
                                val resourceName = friend.avatar_url ?: "e_profile"
                                val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                                if (id != 0) id else context.resources.getIdentifier("e_profile", "drawable", context.packageName)
                            }

                            PersonField(
                                name = friend.username,
                                iconResId = avatarResId,
                                onClick = {
                                    navController.navigate(Routes.anotherPerson(friend.user_id))
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}