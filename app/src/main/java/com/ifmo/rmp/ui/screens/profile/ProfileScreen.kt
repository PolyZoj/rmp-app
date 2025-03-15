package com.ifmo.rmp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.theme.LatoFont
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.EmojiIcon
import com.ifmo.rmp.ui.components.NavigationBar


// Также разобраться как работать с друзьями, пока что немного не понимаю в чем тут их смысл
// Также скорее всего тут надо будет редиркеты делать
// Написать логику


@Composable
fun ProfileScreen() {
    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 12.dp)
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
                EmojiIcon(iconResId = R.drawable.e_profile)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "John Doe", fontSize = 20.sp, fontFamily = LatoFont)
                    Text(text = "Level 5 | 5233 XP", fontSize = 14.sp, color = Color.Gray, fontFamily = LatoFont)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Currently a member of:", fontSize = 12.sp, color = Color.Gray, fontFamily = LatoFont)
                    Text(text = "Mexico52club", fontSize = 14.sp, fontFamily = LatoFont)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Text(text = "Completed Challenges", fontSize = 16.sp, fontFamily = LatoFont)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EmojiIcon(iconResId = R.drawable.e_trophy)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Daily Challenge", fontSize = 14.sp, fontFamily = LatoFont)
                    Text(text = "30-day streak", fontSize = 12.sp, color = Color.Gray, fontFamily = LatoFont)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Quick Goals", fontSize = 16.sp, fontFamily = LatoFont)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EmojiIcon(iconResId = R.drawable.e_step)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Daily Step Goal", fontSize = 14.sp, fontFamily = LatoFont)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EmojiIcon(iconResId = R.drawable.e_water)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Water Intake", fontSize = 14.sp, fontFamily = LatoFont)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Find Friends", fontSize = 14.sp, color = Color.Gray, fontFamily = LatoFont)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "Search for friends", fontSize = 14.sp, color = Color.Gray, fontFamily = LatoFont)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EmojiIcon(iconResId = R.drawable.e_clubs)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Toxa", fontSize = 14.sp, fontFamily = LatoFont)
            }
        }

        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}