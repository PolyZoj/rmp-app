package com.ifmo.rmp.ui.screens.editProfile

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.ifmo.rmp.ui.components.BigButton
import com.ifmo.rmp.ui.components.CustomTextField
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository.getInstance(context) }
    val viewModel = remember { EditProfileViewModel(userRepository) }
    val uiState by viewModel.uiState.collectAsState()

    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", "") ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadUserData(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 32.dp)
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
                text = "User Profile Settings",
                fontFamily = LatoFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            label = "Weight (kg)",
            value = uiState.weight,
            onValueChange = viewModel::onWeightChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            label = "Daily Step Goal (steps)",
            value = uiState.dailyStepGoal,
            onValueChange = viewModel::onStepGoalChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            label = "Water Intake Goal (ml)",
            value = uiState.waterIntakeGoal,
            onValueChange = viewModel::onWaterIntakeChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            label = "Calorie Goal",
            value = uiState.calorieGoal,
            onValueChange = viewModel::onCalorieGoalChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        BigButton(
            text = "Save",
            onClick = { viewModel.saveProfile() }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
