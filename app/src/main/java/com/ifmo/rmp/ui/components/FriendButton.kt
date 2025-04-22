package com.ifmo.rmp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class FriendButtonState {
    AddFriend,
    InviteSent,
    RemoveFriend
}

@Composable
fun FriendButton(
    initialState: FriendButtonState,
    onAddFriend: () -> Unit = {},
    onRemoveFriend: () -> Unit = {}
) {
    var state by remember { mutableStateOf(initialState) }

    when (state) {
        FriendButtonState.AddFriend -> {
            Button(
                onClick = {
                    onAddFriend()
                    state = FriendButtonState.InviteSent
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF228D00),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add Friend", fontSize = 14.sp)
            }
        }

        FriendButtonState.InviteSent -> {
            OutlinedButton(
                onClick = {},
                enabled = false,
                border = BorderStroke(1.dp, Color(0xFF228D00)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF228D00),
                    disabledContentColor = Color(0xFF228D00)
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Invite Sent", fontSize = 14.sp)
            }
        }

        FriendButtonState.RemoveFriend -> {
            OutlinedButton(
                onClick = {
                    onRemoveFriend()
                    state = FriendButtonState.AddFriend
                },
                border = BorderStroke(1.dp, Color(0xFF8B0000)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF8B0000)
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Remove Friend", fontSize = 14.sp)
            }
        }
    }
}

@Preview
@Composable
fun FriendButtonPreview() {
    Column {
        FriendButton(
            initialState = FriendButtonState.AddFriend,
            onAddFriend = { println("Add Friend Clicked") }
        )
        FriendButton(
            initialState = FriendButtonState.InviteSent
        )
        FriendButton(
            initialState = FriendButtonState.RemoveFriend,
            onRemoveFriend = { println("Removed Friend") }
        )
    }
}

