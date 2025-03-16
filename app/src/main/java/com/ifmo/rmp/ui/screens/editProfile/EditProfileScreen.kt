package com.ifmo.rmp.ui.screens.editProfile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.CustomTextField
import com.ifmo.rmp.ui.components.NavigationBar
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit
) {
    var weight by remember { mutableStateOf("52 kg") }
    var dailyStepGoal by remember { mutableStateOf("20000 steps") }
    var waterIntakeGoal by remember { mutableStateOf("2500 ml") }
    var calorieGoal by remember { mutableStateOf("12000 calories") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.e_profile),
                contentDescription = "Profile Photo",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Upload new photo",
                fontFamily = LatoFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {}
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(label = "Weight", value = weight, onValueChange = { weight = it })
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(label = "Daily Step Goal", value = dailyStepGoal, onValueChange = { dailyStepGoal = it })
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(label = "Water Intake Goal", value = waterIntakeGoal, onValueChange = { waterIntakeGoal = it })
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(label = "Calorie Goal", value = calorieGoal, onValueChange = { calorieGoal = it })

        Spacer(modifier = Modifier.weight(1f))

        NavigationBar()
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(onNavigateBack = {})
}