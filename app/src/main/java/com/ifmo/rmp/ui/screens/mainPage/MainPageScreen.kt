package com.ifmo.rmp.ui.screens.mainPage

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifmo.rmp.R
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.BigEmojiButton
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.FriendNotification
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun MainPageScreen(navController: NavController) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository.getInstance(context) }
    val viewModel = remember { MainPageViewModel(userRepository) }

    val showNotifications by viewModel.isNotificationsVisible.collectAsState()
    val friendRequests by viewModel.friendRequests.collectAsState()
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val userId = sharedPreferences.getString("user_id", "") ?: ""

    val userFullName by viewModel.userFullName.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()
    val workouts by viewModel.workouts.collectAsState()
    val stepPercentage by viewModel.stepPercentage.collectAsState()
    val waterPercentage by viewModel.waterPercentage.collectAsState()
    val workoutPercentage by viewModel.workoutPercentage.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
        viewModel.loadDailyStats(context, userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Welcome, $userFullName!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LatoFont
                    )
                    IconButton(
                        onClick = { viewModel.showNotificationsDialog() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BigEmojiButton(
                        emojiResId = R.drawable.e_mainprofile,
                        text = "Profile",
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    BigEmojiButton(
                        emojiResId = R.drawable.e_mainclubs,
                        text = "Clubs",
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    BigEmojiButton(
                        emojiResId = R.drawable.e_workout,
                        text = "Add Workout",
                        onClick = { navController.navigate(Routes.ADD_ACTIVITY) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                }
            }

            item {
                Text(
                    text = "Daily Goal Progress",
                    fontSize = 18.sp,
                    fontFamily = LatoFont
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock(
                        title = "Total Steps",
                        value = steps.toString(),
                        percentage = stepPercentage,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(
                        title = "Water Intake",
                        value = "$waterIntake cups",
                        percentage = waterPercentage,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(
                        title = "Workouts",
                        value = workouts.toString(),
                        percentage = workoutPercentage,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Available Challenges",
                    fontSize = 18.sp,
                    fontFamily = LatoFont
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EmojiAndTextWithDescriptionLine(
                        iconResId = R.drawable.e_step,
                        title = "Morning Yoga Challenge",
                        subtitle = "Intermediate"
                    )
                    EmojiAndTextWithDescriptionLine(
                        iconResId = R.drawable.e_step,
                        title = "10K Steps Daily Challenge",
                        subtitle = "Easy"
                    )
                    EmojiAndTextWithDescriptionLine(
                        iconResId = R.drawable.e_step,
                        title = "Weekly Cardio Challenge",
                        subtitle = "Hard"
                    )
                }
            }
        }

        if (showNotifications) {
            AlertDialog(
                onDismissRequest = { viewModel.hideNotificationsDialog() },
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        if (friendRequests.isEmpty()) {
                            Text(
                                text = "No friend requests",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = friendRequests,
                                    key = { it.user_id } // ВАЖНО: фикс потерь при обновлении
                                ) { friend ->
                                    FriendNotification(
                                        userId = friend.user_id,
                                        userName = friend.username,
                                        onAccept = {
                                            viewModel.acceptRequest(friend.user_id.toInt())
                                        },
                                        onDecline = {
                                            viewModel.declineRequest(friend.user_id.toInt())
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.hideNotificationsDialog() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Close", fontSize = 16.sp)
                    }
                },
                containerColor = Color.White,
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}