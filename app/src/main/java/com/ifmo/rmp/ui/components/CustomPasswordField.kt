package com.ifmo.rmp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun CustomPasswordField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        Text(
            text = label,
            fontFamily = LatoFont,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 5.dp),
        )
        TextField(
            value = value,
            onValueChange = {
                if (!it.contains("\n")) {
                    onValueChange(it)
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF6B6A6A), RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = LatoFont,
                    fontSize = 14.sp,
                    color = Color(0xFF6B6A6A)
                )
            },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomPasswordTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    CustomPasswordField(
        label = "Password",
        value = text,
        onValueChange = { text = it },
        placeholder = "Enter your password"
    )
}
