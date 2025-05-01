package com.ifmo.rmp.ui.screens.addActivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R
import com.ifmo.rmp.ui.components.BigButton

@Composable
fun AddActivityScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
                text = "Add your activity",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Water intake (ml)", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))

        var water by remember { mutableStateOf(100) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { if (water > 0) water -= 50 }) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = Color(0xFF8B0000),
                    modifier = Modifier.size(32.dp)
                )
            }

            OutlinedTextField(
                value = water.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.width(100.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

            IconButton(onClick = { water += 50 }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = Color(0xFF228D00),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BigButton(text = "Add", onClick = { /* TODO */ })

        Spacer(modifier = Modifier.height(32.dp))

        Text("Training results", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Choose training level", fontSize = 16.sp)

        var selectedLevel by remember { mutableStateOf("Easy") }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(68.dp)) {
            listOf("Easy", "Normal", "Hard").forEach { level ->
                val color = when (level) {
                    "Easy" -> Color(0xFF228D00)
                    "Normal" -> Color(0xFFFF9800)
                    "Hard" -> Color(0xFF8B0000)
                    else -> Color.Gray
                }

                OutlinedButton(
                    onClick = { selectedLevel = level },
                    border = BorderStroke(1.dp, color),
                    modifier = Modifier
                        .background(
                            color = if (selectedLevel == level) color.copy(alpha = 0.1f) else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = level,
                        color = color
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Set training time", fontSize = 16.sp)

        var timeInput by remember { mutableStateOf("") }

        OutlinedTextField(
            value = timeInput,
            onValueChange = { timeInput = it },
            placeholder = { Text("Write time in minutes") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default
        )

        Spacer(modifier = Modifier.height(16.dp))
        BigButton(text = "Add training", onClick = { /* TODO */ })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddActivityScreenPreview() {
    AddActivityScreen(onNavigateBack = {})
}