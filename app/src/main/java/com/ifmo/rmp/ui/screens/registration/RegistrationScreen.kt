package com.ifmo.rmp.ui.screens.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.ui.components.CustomTextField
import com.ifmo.rmp.ui.theme.LatoFont

@Preview(showBackground = true)
@Composable
fun Registration() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 100.dp , bottom = 20.dp),
                text = "Join us!",
                fontSize = 24.sp,
                fontFamily = LatoFont
            )
        }
        item {
            CustomTextField(
                "Email",
                email,
                onValueChange = { email = it },
                "Enter your email address"
            )
        }
        item {
            CustomTextField(
                "Password",
                password,
                onValueChange = { password = it },
                "Create a password"
            )
        }
        item {
            CustomTextField(
                "Confirm password",
                confirmPassword,
                onValueChange = { confirmPassword = it },
                /*
                 Add comparing between passwords
                 */
                "Enter your password again"
            )
        }
        item {
            CustomTextField(
                "First name",
                firstName,
                onValueChange = { firstName = it },
                "Enter your first name"
            )
        }
        item {
            CustomTextField(
                "Last name",
                lastName,
                onValueChange = { lastName = it },
                "Enter your last name"
            )
        }
        item {
            CustomTextField(
                "Date of birth",
                dateOfBirth,
                onValueChange = { dateOfBirth = it },
                "Enter your date of birth"
            )
        }
        item {
            CustomTextField(
                "Weight",
                weight,
                onValueChange = { weight = it },
                "Enter your weight"
            )
        }
        item {
            CustomTextField(
                "Height",
                weight,
                onValueChange = { weight = it },
                "Enter your weight"
            )
        }
    }
}