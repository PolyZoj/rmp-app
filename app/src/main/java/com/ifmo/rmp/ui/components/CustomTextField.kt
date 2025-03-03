package com.ifmo.rmp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 2.dp,),
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF6B6A6A), RoundedCornerShape(8.dp))
                .padding(horizontal = 0.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = Color(0xFF6B6A6A)
                )
            },
            shape = RoundedCornerShape(8.dp),
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
fun CustomTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    CustomTextField(
        label = "Email",
        value = text,
        onValueChange = { text = it },
        placeholder = "Enter your email address"
    )
}
