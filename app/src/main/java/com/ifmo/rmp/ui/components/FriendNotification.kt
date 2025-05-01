package com.ifmo.rmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FriendNotification(
    userId: String,
    userName: String,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }

    if (!isVisible) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                append("User ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(userName)
                }
            },
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "wants to add you as a friend",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Accept",
                tint = Color(0xFF228D00),
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        isVisible = false
                        onAccept(userId)
                    }
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Decline",
                tint = Color(0xFF8B0000),
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        isVisible = false
                        onDecline(userId)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendNotificationPreview() {
    FriendNotification(
        userId = "123",
        userName = "KaiAngel",
        onAccept = { },
        onDecline = { }
    )
}
