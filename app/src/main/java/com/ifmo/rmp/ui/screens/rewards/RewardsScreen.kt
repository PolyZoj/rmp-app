package com.ifmo.rmp.ui.screens.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.RewardsLine
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun RewardsScreen() {
    val rewards = listOf(
        RewardData(
            iconResId = R.drawable.e_trophy,
            title = "10K Steps",
            subtitle = "Daily challenge completed",
            status = "Completed"
        ),
        RewardData(
            iconResId = R.drawable.e_rewards,
            title = "Streak Master",
            subtitle = "Logged in 7 days in a row",
            status = "Completed"
        ),
        RewardData(
            iconResId = R.drawable.e_water,
            title = "Hydration Hero",
            subtitle = "Drank 2L water today",
            status = "In Progress"
        ),
        RewardData(
            iconResId = R.drawable.e_step,
            title = "Eco Walker",
            subtitle = "Walked instead of driving",
            status = "In Progress"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 36.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Your Achievements", fontSize = 22.sp, fontFamily = LatoFont)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Achievements", fontSize = 18.sp, fontFamily = LatoFont)

        Spacer(modifier = Modifier.height(4.dp))

        rewards.forEach { reward ->
            RewardsLine(
                iconResId = reward.iconResId,
                title = reward.title,
                subtitle = reward.subtitle,
                status = reward.status
            )
        }
    }
}

data class RewardData(
    val iconResId: Int,
    val title: String,
    val subtitle: String,
    val status: String
)

@Preview(showBackground = true)
@Composable
fun RewardsScreenPreview() {
    RewardsScreen()
}
