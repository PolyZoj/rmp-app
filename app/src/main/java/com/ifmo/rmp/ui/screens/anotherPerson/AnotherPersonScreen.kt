package com.ifmo.rmp.ui.screens.anotherPerson

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.EmojiIcon
import com.ifmo.rmp.ui.components.FriendButton
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.theme.LatoFont
import kotlinx.coroutines.launch

@Composable
fun AnotherPersonScreen(
    onNavigateBack: () -> Unit,
    userId: String? = null,
    onFriendActionClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val actualUserId = userId ?: sharedPreferences.getString("user_id", "") ?: ""

    val userRepository = remember { UserRepository.getInstance(context) }
    val viewModel = remember { AnotherPersonViewModel(userRepository) }

    val userState by viewModel.user.collectAsState()
    val friendButtonState by viewModel.friendButtonState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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

    LaunchedEffect(actualUserId) {
        viewModel.loadUser(actualUserId)
        viewModel.loadStats(context, actualUserId)
    }

    LaunchedEffect(userState?.club_id) {
        if (userState?.club_id != null){
            userState?.club_id?.let {
                viewModel.loadClubName(context, it)
            }
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 12.dp)
                .padding(top = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.e_arrow_left),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onNavigateBack() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "User Profile",
                        fontFamily = LatoFont,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val avatarResId = remember(userState?.avatar_url) {
                        val resourceName = userState?.avatar_url ?: "e_profile"
                        val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                        if (id != 0) id else context.resources.getIdentifier("e_profile", "drawable", context.packageName)
                    }
                    EmojiIcon(iconResId = avatarResId)
                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = userState?.username ?: "Loading...",
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

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FriendButton(
                        state = friendButtonState,
                        onAddFriend = {
                            val userId = userState?.user_id
                            if (userId != null && userId.isNotEmpty()) {
                                try {
                                    viewModel.addFriend(userId.toInt())
                                    onFriendActionClick()
                                } catch (e: NumberFormatException) {
                                    // Handle invalid user ID format
                                    viewModel.clearError()
                                }
                            }
                        },
                        onRemoveFriend = {
                            val userId = userState?.user_id
                            if (userId != null && userId.isNotEmpty()) {
                                try {
                                    viewModel.removeFriend(userId.toInt())
                                    onFriendActionClick()
                                } catch (e: NumberFormatException) {
                                    // Handle invalid user ID format
                                    viewModel.clearError()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}