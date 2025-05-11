package com.ifmo.rmp.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ifmo.rmp.R
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.EmojiIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClubScreen(
    navController: NavController,
    clubId: String,
    viewModel: ClubsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var newMemberId by remember { mutableStateOf("") }
    
    val userRepository = remember { UserRepository.getInstance(context) }
    
    val memberUsers = remember { mutableStateMapOf<String, UserDtoResponse?>() }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(clubId) {
        if (clubId.isNotBlank()) {
            viewModel.getClubInfo(context, clubId)
        }
    }
    
    LaunchedEffect(uiState.clubInfo) {
        uiState.clubInfo?.members?.forEach { memberId ->
            if (!memberUsers.containsKey(memberId)) {
                coroutineScope.launch {
                    val result = userRepository.getUserData(memberId)
                    result.onSuccess { userData ->
                        memberUsers[memberId] = userData
                    }
                }
            }
        }
    }
    
    val clubIconResId = remember {
        val id = context.resources.getIdentifier("e_clubs", "drawable", context.packageName)
        if (id != 0) id else R.drawable.e_profile
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Club", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val club = uiState.clubInfo
            
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (club != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Club basic info
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
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Club Avatar
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
                                
                                // Club basic info
                                Column {
                                    Text(
                                        text = club.name ?: "Unnamed Club",
                                        fontSize = 20.sp,
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
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Club Management",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Add New Member",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = newMemberId,
                                    onValueChange = { newMemberId = it },
                                    label = { Text("User ID") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Button(
                                    onClick = {
                                        if (newMemberId.isNotBlank()) {
                                            viewModel.addMember(context, clubId, newMemberId)
                                            newMemberId = ""
                                        }
                                    },
                                    enabled = newMemberId.isNotBlank()
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Members (${club.members?.size ?: 0})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (club.members.isNullOrEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No members yet",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp)
                            ) {
                                items(club.members) { memberId ->
                                    val memberData = memberUsers[memberId]
                                    MemberItem(
                                        memberId = memberId,
                                        userData = memberData,
                                        isOwner = club.ownerId == memberId,
                                        isLoading = !memberUsers.containsKey(memberId),
                                        onRemove = {
                                            viewModel.removeMember(context, clubId, memberId)
                                        }
                                    )
                                    Divider()
                                }
                            }
                        }
                    }
                    
                    if (uiState.errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                    
                    if (uiState.lastMemberOperation != null) {
                        LaunchedEffect(uiState.lastMemberOperation) {
                            kotlinx.coroutines.delay(3000)
                            viewModel.clearLastMemberOperation()
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.lastMemberOperation?.message ?: "",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else if (!uiState.isLoading && uiState.errorMessage.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error loading club",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigateUp() }
                    ) {
                        Text("Go Back")
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItem(
    memberId: String,
    userData: UserDtoResponse?,
    isOwner: Boolean,
    isLoading: Boolean,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Loading user information...",
                    color = Color.Gray
                )
            }
        } else if (userData != null) {
            // Display user information
            Row(verticalAlignment = Alignment.CenterVertically) {
                // User Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userData.first_name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "${userData.first_name} ${userData.last_name}",
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = "@${userData.username}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    
                    if (isOwner) {
                        Text(
                            text = "Owner",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            if (!isOwner) {
                IconButton(
                    onClick = onRemove
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove member",
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = memberId.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "User ID: $memberId",
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = "Unable to load user details",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                    
                    if (isOwner) {
                        Text(
                            text = "Owner",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            // Only show remove button for non-owners
            if (!isOwner) {
                IconButton(
                    onClick = onRemove
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove member",
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageClubScreenPreview() {
    ManageClubScreen(
        navController = rememberNavController(),
        clubId = "1"
    )
} 