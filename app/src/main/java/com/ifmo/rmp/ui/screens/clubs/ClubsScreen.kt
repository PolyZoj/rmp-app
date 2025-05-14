package com.ifmo.rmp.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ifmo.rmp.R
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.ui.components.EmojiIcon
import com.ifmo.rmp.ui.components.SearchBar
import com.ifmo.rmp.ui.navigation.Routes
import android.content.Context
import com.ifmo.rmp.ui.theme.LatoFont


@Composable
fun ClubsScreen(
    navController: NavController,
    viewModel: ClubsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Get the current user ID from SharedPreferences
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
    
    // Get a safe club icon resource ID
    val clubIconResId = remember {
        val id = context.resources.getIdentifier("e_clubs", "drawable", context.packageName)
        if (id != 0) id else R.drawable.e_profile // Fallback to profile icon if clubs icon is missing
    }
    
    // State for the add member dialog
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var dialogClubId by remember { mutableStateOf("") }
    var usernameInput by remember { mutableStateOf("") }
    
    // Load clubs list when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.getClubsList(context)
        // Load current club from user profile
        viewModel.getClubInfo(context)
    }

    // Add member dialog
    if (showAddMemberDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddMemberDialog = false
                usernameInput = ""
            },
            title = { Text("Add Member by Username") },
            text = {
                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (usernameInput.isNotBlank()) {
                            viewModel.addMemberByUsername(context, dialogClubId, usernameInput)
                            showAddMemberDialog = false
                            usernameInput = ""
                        }
                    },
                    enabled = usernameInput.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showAddMemberDialog = false 
                        usernameInput = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 60.dp)
    ) {
        // Current Club Section
        Text(
            text = "Your current club",
            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display current club if available
        val currentClub = uiState.clubInfo
        val hasJoinedClub = currentClub != null && currentClub.members.contains(currentUserId) && currentClub.id != "0"
        
        if (currentClub != null && currentClub.id != "0") {
            // Check if user is in the members list
            val isMember = currentClub.members.contains(currentUserId)
            
            if (isMember) {
                println("Displaying current club: ${currentClub.name}, User is member: $isMember")
                ClubItemDetailed(
                    club = currentClub,
                    clubIconResId = clubIconResId,
                    isMember = true,
                    onManageClick = {
                        // Navigate to club management screen
                        println("Navigating to club management screen for club ID: ${currentClub.id}")
                        println("Routes.manageClub: ${currentClub}")
                        currentClub.id?.let { clubId ->
                            navController.navigate(Routes.manageClub(clubId))
                        }
                    },
                    onLeaveClick = {
                        // Leave the club if the current user is not the owner
                        // if (currentClub.ownerId != currentUserId) {
                            currentClub.id?.let { clubId ->
                                viewModel.leaveClub(context, clubId)
                            }
                        // }
                    }
                )
            } else {
                // If the club is loaded but user is not a member
                Text(
                    text = "You haven't joined any club yet",
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else {
            // Placeholder for when no club is joined
            Text(
                text = "You haven't joined any club yet",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Only show "Find club" section and club list if user hasn't joined any club
        if (!hasJoinedClub) {
            Spacer(modifier = Modifier.height(24.dp))

            // Find Club Section with Search
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Find club",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                
                CreateClubButton {
                    // Navigate to create club screen
                    navController.navigate(Routes.CREATE_CLUB)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Search Bar
//            SearchBar(
//                hint = "Search for clubs...",
//                onSearchClick = { searchQuery ->
//                    // Handle search
//                }
//            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // List of clubs
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.clubsList.isNotEmpty()) {
                LazyColumn {
                    items(uiState.clubsList) { club ->
                        ClubItemWithJoinButton(
                            club = club,
                            clubIconResId = clubIconResId,
                            onClick = {
                                // Explicitly pass the club ID when navigating to the club management screen
                                if (club.id != null && club.id.isNotBlank()) {
                                    viewModel.getClubInfo(context, club.id)
                                    navController.navigate(Routes.manageClub(club.id))
                                }
                            },
                            onJoin = {
                                if (club.ownerId == currentUserId) {
                                    // Club owner adding a member - show dialog
                                    dialogClubId = club.id ?: ""
                                    showAddMemberDialog = true
                                } else {
                                    // Regular user joining a club
                                    viewModel.addMember(context, club.id ?: "", currentUserId)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else if (!uiState.isLoading && uiState.errorMessage.isEmpty()) {
                Text(
                    text = "No clubs found",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            // Show a message that the user can leave their current club if they want to join another
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "You need to leave your current club to join another one",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 17.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun ClubItem(
    club: ClubInfoResponse,
    clubIconResId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCCCC)), // Light red background
            contentAlignment = Alignment.Center
        ) {
            EmojiIcon(
                iconResId = clubIconResId,
                size = 32
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = club.name ?: "Unnamed Club",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = club.description ?: "No description",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ClubItemDetailed(
    club: ClubInfoResponse,
    clubIconResId: Int,
    isMember: Boolean,
    onManageClick: () -> Unit,
    onLeaveClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFCCCC)),
                        contentAlignment = Alignment.Center
                    ) {
                        EmojiIcon(
                            iconResId = clubIconResId,
                            size = 32
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = club.name ?: "Unnamed Club",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = club.description ?: "No description",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                if (club.ownerId.equals(currentUserId)) {
                    IconButton(onClick = onManageClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Manage club",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClubStat(label = "Members", value = "${club.members?.size ?: 0}")
                ClubStat(label = "Owner", value = if (club.ownerId?.isNotEmpty() == true) "ID: ${club.ownerId}" else "Unknown")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onManageClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Manage club"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manage")
                }
                
                if (onLeaveClick != null) {
                    Button(
                        onClick = onLeaveClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Leave club"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Leave")
                    }
                }
            }
        }
    }
}

@Composable
fun ClubStat(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ClubItemWithJoinButton(
    club: ClubInfoResponse,
    clubIconResId: Int,
    onClick: () -> Unit,
    onJoin: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
    
    // Determine if user is already a member
    val isJoined = club.isJoined == true || club.members.contains(currentUserId)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCCCC)),
                contentAlignment = Alignment.Center
            ) {
                EmojiIcon(
                    iconResId = clubIconResId,
                    size = 32
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = club.name ?: "Unnamed Club",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = club.description ?: "No description",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        if (isJoined) {
            Text(
                text = "Joined",
                color = Color.Green,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            // Show button for all clubs where the user is not a member
            Button(
                onClick = onJoin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                if (club.ownerId == currentUserId) {
                    Text("Add Member", fontSize = 14.sp)
                } else {
                    Text("Join", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun CreateClubButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create club",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Create club",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubsScreenPreview() {
    ClubsScreen(navController = rememberNavController())
}
