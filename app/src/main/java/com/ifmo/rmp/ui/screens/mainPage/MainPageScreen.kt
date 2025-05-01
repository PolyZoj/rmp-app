package com.ifmo.rmp.ui.screens.mainPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.BigEmojiButton
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.components.FriendNotification
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont

// доделать скролл + если есть запросы непрочитанные, то пометить(Например красной точкой или обводкой???)
// подключить в навигацию

@Composable
fun MainPageScreen(navController: NavController) {
    val showNotifications = remember { mutableStateOf(false) }

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
                        text = "Welcome, Vasya Pupkin!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LatoFont
                    )
                    IconButton(
                        onClick = { showNotifications.value = true }
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
                    Box(modifier = Modifier.weight(1f)) {
                        BigEmojiButton(
                            emojiResId = R.drawable.e_mainprofile,
                            text = "Profile",
                            onClick = {  },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BigEmojiButton(
                            emojiResId = R.drawable.e_mainclubs,
                            text = "Clubs",
                            onClick = {  },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BigEmojiButton(
                            emojiResId = R.drawable.e_workout,
                            text = "Add Workout",
                            onClick = { navController.navigate(Routes.ADD_ACTIVITY) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
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
                    InfoBlock(title = "Total Steps", value = "15,000", percentage = 5, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(title = "Water Intake", value = "8 cups", percentage = -10, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(1.dp))
                    InfoBlock(title = "Workouts", value = "20", percentage = 15, modifier = Modifier.weight(1f))
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

        if (showNotifications.value) {
            AlertDialog(
                onDismissRequest = { showNotifications.value = false },
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
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                FriendNotification(
                                    userId = "101",
                                    userName = "ZZZ",
                                    onAccept = { println("Accepted 101") },
                                    onDecline = { println("Declined 101") }
                                )
                            }
                            item {
                                FriendNotification(
                                    userId = "102",
                                    userName = "9mice",
                                    onAccept = { println("Accepted 102") },
                                    onDecline = { println("Declined 102") }
                                )
                            }
                            item {
                                FriendNotification(
                                    userId = "103",
                                    userName = "mrKent228",
                                    onAccept = { println("Accepted 103") },
                                    onDecline = { println("Declined 103") }
                                )
                            }
                            item {
                                FriendNotification(
                                    userId = "104",
                                    userName = "SamsaUZB",
                                    onAccept = { println("Accepted 104") },
                                    onDecline = { println("Declined 104") }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showNotifications.value = false },
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
