package com.ifmo.rmp.ui.screens.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ifmo.rmp.ui.components.BigButton
import com.ifmo.rmp.ui.components.CustomPasswordField
import com.ifmo.rmp.ui.components.CustomTextField
import com.ifmo.rmp.ui.theme.AppTypography
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
//    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                viewModel.onDateOfBirthChange(formatter.format(date))
                showDatePicker = false
            },
            year,
            month,
            day
        ).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Create Account",
                style = AppTypography.titleLarge,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(top = 80.dp, bottom = 20.dp)
            )
        }

        item {
            CustomTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                placeholder = "Enter your email",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.username,
                onValueChange = viewModel::onUsernameChange,
                label = "Username",
                placeholder = "Enter your username",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.firstName,
                onValueChange = viewModel::onFirstNameChange,
                label = "First Name",
                placeholder = "Enter first name",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChange,
                label = "Last Name",
                placeholder = "Enter last name",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.dateOfBirth,
                onValueChange = { showDatePicker = true },
                label = "Date of Birth",
                placeholder = "Click to select date",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.weight,
                onValueChange = viewModel::onWeightChange,
                label = "Weight",
                placeholder = "Enter weight in kg",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.height,
                onValueChange = viewModel::onHeightChange,
                label = "Height",
                placeholder = "Enter height in cm",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.stepGoal,
                onValueChange = viewModel::onStepGoalChange,
                label = "Daily Step Goal",
                placeholder = "Enter your daily step goal",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.waterIntake,
                onValueChange = viewModel::onWaterIntakeChange,
                label = "Water Intake Goal",
                placeholder = "Enter daily water intake in ml",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomTextField(
                value = uiState.calorieGoal,
                onValueChange = viewModel::onCalorieGoalChange,
                label = "Calorie Goal",
                placeholder = "Enter daily calorie goal",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomPasswordField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password",
                placeholder = "Create a password",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            CustomPasswordField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm Password",
                placeholder = "Repeat your password",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            if (uiState.errorMessage.isNotEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = AppTypography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        item {
            BigButton(
                text = if (uiState.isLoading) "Processing..." else "Create Account",
                onClick = { if (!uiState.isLoading) viewModel.register(context) },
            )
        }

//        TextButton(
//            onClick = { navController.navigate("login") },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = "Already have an account? Log in",
//                style = AppTypography.bodyLarge
//            )
//        }
    }
}