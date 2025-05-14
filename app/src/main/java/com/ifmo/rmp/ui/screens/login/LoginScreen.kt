package com.ifmo.rmp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ifmo.rmp.ui.components.BigButton
import com.ifmo.rmp.ui.components.CustomPasswordField
import com.ifmo.rmp.ui.components.CustomTextField
import androidx.navigation.NavController
import com.ifmo.rmp.ui.navigation.Routes

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.isSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 30.dp),
            text = "Welcome to PolyZoj community!",
            fontSize = 24.sp,
        )

        CustomTextField(
            label = "Username",
            value = uiState.username,
            onValueChange = { if (!uiState.isLoading) viewModel.onUsernameChange(it) },
            placeholder = "Enter your username"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomPasswordField(
            label = "Password",
            value = uiState.password,
            onValueChange = { if (!uiState.isLoading) viewModel.onPasswordChange(it) },
            placeholder = "Enter your password"
        )

        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 17.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        BigButton(
            text = if (uiState.isLoading) "Processing..." else "Log in",
            onClick = { if (!uiState.isLoading) viewModel.login(context) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Don't have an account?",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Routes.REGISTRATION) },
            text = "Register",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}