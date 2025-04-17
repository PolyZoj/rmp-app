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
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.BigEmojiButton
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.theme.LatoFont

// доделать скролл + если есть запросы непрочитанные, то пометить(Например красной точкой или обводкой???)
// подключить в навигацию

@Composable
fun MainPageScreen() {
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
                            emojiResId = R.drawable.e_trophy,
                            text = "Profile",
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BigEmojiButton(
                            emojiResId = R.drawable.e_trophy,
                            text = "Social",
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BigEmojiButton(
                            emojiResId = R.drawable.e_trophy,
                            text = "Add friend",
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
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
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

@Preview(showBackground = true)
@Composable
fun MainPageScreenPreview() {
    MainPageScreen()
}