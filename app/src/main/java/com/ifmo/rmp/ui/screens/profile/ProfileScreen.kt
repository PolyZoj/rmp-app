package com.ifmo.rmp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.theme.LatoFont
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.EmojiIcon
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.GoalBox
import com.ifmo.rmp.ui.components.PersonField
import com.ifmo.rmp.ui.components.SearchBar

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    navController: NavController
) {
    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EmojiIcon(
                    iconResId = R.drawable.e_profile,
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.clickable { onNavigateToEditProfile() }
                ) {
                    Text(text = "John Doe", fontSize = 20.sp, fontFamily = LatoFont)
                    Text(text = "Level 5 | 5233 XP", fontSize = 14.sp, color = Color.Gray, fontFamily = LatoFont)
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Currently a member of:", fontSize = 14.sp, color = Color.Gray, fontFamily = LatoFont)
                    Text(text = "Mexico52club", fontSize = 16.sp, fontFamily = LatoFont)
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )

                GoalBox(
                    iconResId = R.drawable.e_water,
                    text = "Water Intake",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Find Friends", fontSize = 18.sp, fontFamily = LatoFont)
            Spacer(modifier = Modifier.height(4.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(8.dp))

            val friends: List<Pair<String, Int>> = listOf(
                "Toxa" to R.drawable.e_profile,
                "Roma" to R.drawable.e_profile,
                "9mice" to R.drawable.e_profile,
                "Roman52" to R.drawable.e_profile,
                "RomanPPPiroman" to R.drawable.e_profile
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 42.dp)
                ) {
                    items(friends) { friend ->
                        PersonField(
                            name = friend.first,
                            iconResId = friend.second,
                            onClick = {
                                navController.navigate("another_person/123")
                            }
                        )
                    }
                }
            }

        }
    }
}
