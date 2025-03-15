package com.ifmo.rmp.ui.screens.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ifmo.rmp.ui.components.BigButton
import com.ifmo.rmp.ui.components.CustomPasswordField
import com.ifmo.rmp.ui.components.CustomTextField
import com.ifmo.rmp.ui.theme.LatoFont

@Preview(showBackground = true)
@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 50.dp, bottom = 20.dp),
                text = "Join us!",
                fontSize = 24.sp,
                fontFamily = LatoFont
            )
        }
        item {
            RegistrationTextField(
                "Email",
                uiState.email,
                viewModel::onEmailChange,
                "Enter your email"
            )
        }
        item {
            RegistrationPasswordField(
                "Password",
                uiState.password,
                viewModel::onPasswordChange,
                "Create a password"
            )
        }
        item {
            RegistrationPasswordField(
                "Confirm Password",
                uiState.confirmPassword,
                viewModel::onConfirmPasswordChange,
                "Repeat your password"
            )
        }
        item {
            RegistrationTextField(
                "First Name",
                uiState.firstName,
                viewModel::onFirstNameChange,
                "Enter your first name"
            )
        }
        item {
            RegistrationTextField(
                "Last Name",
                uiState.lastName,
                viewModel::onLastNameChange,
                "Enter your last name"
            )
        }
        item {
            RegistrationTextField(
                "Date of Birth",
                uiState.dateOfBirth,
                viewModel::onDateOfBirthChange,
                "Enter your date of birth"
            )
        }
        item {
            RegistrationTextField(
                "Weight",
                uiState.weight,
                viewModel::onWeightChange,
                "Enter your weight"
            )
        }
        item {
            RegistrationTextField(
                "Height",
                uiState.height,
                viewModel::onHeightChange,
                "Enter your height"
            )
        }
        item {
            RegistrationTextField(
                "Daily Step Goal",
                uiState.stepGoal,
                viewModel::onStepGoalChange,
                "Set your step goal"
            )
        }
        item {
            RegistrationTextField(
                "Water Intake Goal",
                uiState.waterIntake,
                viewModel::onWaterIntakeChange,
                "Set your water intake goal"
            )
        }

        item {
            BigButton("Create Account", onClick = viewModel::register)
        }

        item {
            Text(
                text = uiState.errorMessage,
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun RegistrationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    CustomTextField(
        modifier = Modifier.padding(vertical = 5.dp),
        label,
        value,
        onValueChange,
        placeholder
    )
}

@Composable
fun RegistrationPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    CustomPasswordField(
        modifier = Modifier.padding(vertical = 5.dp),
        label,
        value,
        onValueChange,
        placeholder
    )
}