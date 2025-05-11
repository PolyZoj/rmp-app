package com.ifmo.rmp.ui.screens.anotherPerson

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
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
import com.ifmo.rmp.ui.components.FriendButtonState
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.theme.LatoFont
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnotherPersonScreen(
    onNavigateBack: () -> Unit,
    userId: String? = null,
    friendState: FriendButtonState,
    onFriendActionClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val actualUserId = userId ?: sharedPreferences.getString("user_id", "") ?: ""

    val userRepository = remember { UserRepository.getInstance(context) }
    val viewModel = remember { AnotherPersonViewModel(userRepository) }

    val userState by viewModel.user.collectAsState()

    LaunchedEffect(actualUserId) {
        viewModel.loadUser(actualUserId)
    }

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
                val userAvatarResId = remember(userState?.avatar_url) {
                    val name = userState?.avatar_url ?: "e_profile"
                    context.resources.getIdentifier(name, "drawable", context.packageName)
                }
                EmojiIcon(iconResId = userAvatarResId)
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = userState?.username ?: "Loading...",
                        fontSize = 20.sp,
                        fontFamily = LatoFont
                    )
                    Text(
                        text = "Level 7 | 5252 XP",
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
                        text = userState?.club_id?.toString() ?: "No club",
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
                InfoBlock(
                    title = "Total Steps",
                    value = "454",
                    percentage = -40,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(1.dp))
                InfoBlock(
                    title = "Water Intake",
                    value = "10 cups",
                    percentage = 3,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(1.dp))
                InfoBlock(
                    title = "Workouts",
                    value = "1",
                    percentage = 12,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                FriendButton(
                    initialState = friendState,
                    onAddFriend = onFriendActionClick
                )
            }
        }
    }
}
