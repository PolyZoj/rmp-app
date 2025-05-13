package com.ifmo.rmp.ui.screens.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ifmo.rmp.R
import com.ifmo.rmp.data.repository.StatsRepository
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.BigInfoBlock
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.GoalBox
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont
import kotlinx.coroutines.launch

@Composable
fun ActivitiesScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", "") ?: ""
    val viewModel = remember {
        ActivitiesViewModel(
            UserRepository.getInstance(context),
            StatsRepository.getInstance(context)
        )
    }
    val steps by viewModel.steps.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()
    val workouts by viewModel.workouts.collectAsState()
    val calories by viewModel.calories.collectAsState()
    val stepGoal by viewModel.stepGoal.collectAsState()
    val waterGoal by viewModel.waterGoal.collectAsState()
    val workoutGoal by viewModel.workoutGoal.collectAsState()
    val calorieGoal by viewModel.calorieGoal.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val friendsActivity by viewModel.friendsActivity.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Проверка и запрос разрешения ACTIVITY_RECOGNITION
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            viewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadUserData(context, userId)
            viewModel.loadStats(context, userId)
            viewModel.loadFriendsActivity(context)
        } else {
            viewModel.clearError()
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Activity Summary", fontSize = 22.sp, fontFamily = LatoFont)

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    BigInfoBlock(
                        title = "Steps taken",
                        value = when {
                            isLoading -> "Loading..."
                            steps == -1 -> "Sensor unavailable"
                            !hasPermission -> "Permission denied"
                            else -> "$steps / $stepGoal"
                        }
                    )
                    BigInfoBlock(
                        title = "Water intake",
                        value = if (isLoading) "Loading..." else "$waterIntake / $waterGoal"
                    )
                }

                if (!hasPermission) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Grant Step Tracking Permission")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    BigInfoBlock(
                        title = "Workouts completed",
                        value = if (isLoading) "Loading..." else "$workouts / $workoutGoal"
                    )
                    BigInfoBlock(
                        title = "Calories burned",
                        value = if (isLoading) "Loading..." else "$calories / $calorieGoal"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GoalBox(
                    iconResId = R.drawable.e_workout,
                    text = "Add workout",
                    onClick = { navController.navigate(Routes.ADD_ACTIVITY) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .padding(start = 8.dp)
                )

                GoalBox(
                    iconResId = R.drawable.e_water,
                    text = "Add water intake",
                    onClick = { navController.navigate(Routes.ADD_ACTIVITY) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .padding(end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Friends' Activity", fontSize = 18.sp, fontFamily = LatoFont)

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (friendsActivity.isEmpty() && !isLoading) {
                    Text(
                        text = "You don't have any friends yet, add some!",
                        fontSize = 16.sp,
                        fontFamily = LatoFont,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(friendsActivity) { friend ->
                            val avatarResId = remember(friend.avatarUrl) {
                                val resourceName = friend.avatarUrl
                                val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                                if (id != 0) id else context.resources.getIdentifier("e_profile", "drawable", context.packageName)
                            }
                            EmojiAndTextWithDescriptionLine(
                                iconResId = avatarResId,
                                title = friend.username,
                                subtitle = "Steps: ${friend.steps} | Calories: ${friend.calories}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                                    .clickable { navController.navigate(Routes.anotherPerson(friend.userId)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActivitiesScreenPreview() {
    ActivitiesScreen(navController = NavController(context = LocalContext.current))
}