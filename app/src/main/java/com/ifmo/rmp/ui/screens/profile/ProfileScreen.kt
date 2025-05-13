package com.ifmo.rmp.ui.screens.profile

import android.content.Context
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
import com.ifmo.rmp.data.model.getDrawableResId
import com.ifmo.rmp.data.repository.ChallengesRepository
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
    val challengesRepository = remember { ChallengesRepository.getInstance(context) }
    val viewModel = remember { ProfileViewModel(userRepository, challengesRepository) }

    val user by viewModel.user.collectAsState()
    val achievements by viewModel.challengesList.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val steps by viewModel.steps.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()
    val workouts by viewModel.workouts.collectAsState()
    val stepPercentage by viewModel.stepPercentage.collectAsState()
    val waterPercentage by viewModel.waterPercentage.collectAsState()
    val workoutPercentage by viewModel.workoutPercentage.collectAsState()
    val level by viewModel.level.collectAsState()
    val xp by viewModel.xp.collectAsState()
    val clubName by viewModel.clubName.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(sharedPreferences) {
        viewModel.loadUser(userId)
    }

    LaunchedEffect(sharedPreferences) {
        viewModel.loadFriends()
    }

    LaunchedEffect(sharedPreferences) {
        viewModel.loadStats(context, userId)
    }

    LaunchedEffect(user?.club_id) {
        user?.club_id?.let {
            viewModel.loadClubName(context, it)
        }
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
                .padding(horizontal = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val userAvatarResId = remember(user?.avatar_url) {
                        val resourceName = user?.avatar_url ?: "e_profile"
                        val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                        if (id != 0) id else context.resources.getIdentifier("e_profile", "drawable", context.packageName)
                    }
                    EmojiIcon(iconResId = userAvatarResId, 36)
                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.clickable { onNavigateToEditProfile() }) {
                        Text(
                            text = user?.username ?: "Unknown",
                            fontSize = 20.sp,
                            fontFamily = LatoFont
                        )
                        Text(
                            text = "Level $level | $xp XP",
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
                            text = clubName,
                            fontSize = 16.sp,
                            fontFamily = LatoFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Daily Statistics", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock(
                        title = "Total Steps",
                        value = if (isLoading) "Loading..." else "$steps",
                        percentage = stepPercentage,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(
                        title = "Water Intake",
                        value = if (isLoading) "Loading..." else "$waterIntake",
                        percentage = waterPercentage,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(
                        title = "Workouts",
                        value = if (isLoading) "Loading..." else "$workouts",
                        percentage = workoutPercentage,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Completed Challenges", fontSize = 18.sp, fontFamily = LatoFont)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(achievements.take(10)) { achievement ->
                            if (achievement.status == "COMPLETED") {
                                EmojiAndTextWithDescriptionLine(
                                    iconResId = getDrawableResId(achievement.icon),
                                    title = achievement.title,
                                    subtitle = achievement.description,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp)
                                )
                            }
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
                                popUpTo(navController.graph.startDestinationId) {
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
                                popUpTo(navController.graph.startDestinationId) {
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

                val displayedFriends = if (searchQuery.isEmpty()) friends else searchResults

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