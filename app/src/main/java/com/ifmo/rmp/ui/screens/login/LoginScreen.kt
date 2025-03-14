package com.ifmo.rmp.ui.screens.login

import androidx.compose.foundation.layout.*
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
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 30.dp),
            text = "Welcome to PolyZoj community!",
            fontSize = 24.sp,
        )

        CustomTextField(
            label = "Email",
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = "Enter your email address"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomPasswordField(
            label = "Password",
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = "Enter your password"
        )

        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                text = uiState.errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp, start = 15.dp, end = 15.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        BigButton("Log in", onClick = { viewModel.login() })

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Don't have an account?",
            textAlign = TextAlign.Center,
            fontFamily = LatoFont,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Register",
            textAlign = TextAlign.Center,
            fontFamily = LatoFont,
            fontSize = 16.sp
        )
    }
}
