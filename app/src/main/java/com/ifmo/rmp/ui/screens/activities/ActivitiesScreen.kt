package com.ifmo.rmp.ui.screens.activities

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.BigInfoBlock
import com.ifmo.rmp.ui.components.EmojiAndTextWithDescriptionLine
import com.ifmo.rmp.ui.components.GoalBox
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun ActivitiesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                BigInfoBlock(title = "Steps taken", value = "10000 / 20000")
                BigInfoBlock(title = "Water intake", value = "8 cups / 12 cups")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                BigInfoBlock(title = "Workouts completed", value = "2")
                BigInfoBlock(title = "Calories burned", value = "500 / 5000")
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            EmojiAndTextWithDescriptionLine(
                iconResId = R.drawable.e_step,
                title = "John Doe",
                subtitle = "Steps: 8000, Calories: 1200"
            )
            EmojiAndTextWithDescriptionLine(
                iconResId = R.drawable.e_step,
                title = "Jane Doe",
                subtitle = "Steps: 12000, Calories: 790"
            )
        }
    }
}