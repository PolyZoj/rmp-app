package com.ifmo.rmp.ui.screens.rewards

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.data.model.getDrawableResId
import com.ifmo.rmp.data.repository.ChallengesRepository
import com.ifmo.rmp.ui.components.RewardsLine
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun RewardsScreen() {

    val context = LocalContext.current
    val challengesRepository = remember { ChallengesRepository.getInstance(context) }
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val userId = sharedPreferences.getString("user_id", "") ?: ""
    val viewModel = remember { RewardsVewModel(challengesRepository) }

    val achievements by viewModel.challengesList.collectAsState()

    LaunchedEffect(sharedPreferences) {
        viewModel.loadChallenges(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Your Achievements", fontSize = 22.sp, fontFamily = LatoFont)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Achievements", fontSize = 18.sp, fontFamily = LatoFont)

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            this@LazyColumn.items(achievements.take(10)) { achievement ->
                RewardsLine(
                    iconResId = getDrawableResId(achievement.icon),
                    title = achievement.title,
                    subtitle = achievement.description,
                    status = achievement.status
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsScreenPreview() {
    RewardsScreen()
}
