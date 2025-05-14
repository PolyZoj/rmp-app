package com.ifmo.rmp.ui.screens.clubs

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.ifmo.rmp.data.model.StatsResponse
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.StatsRepository
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.EmojiIcon
import com.ifmo.rmp.ui.components.InfoBlock
import com.ifmo.rmp.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClubScreen(
    navController: NavController,
    clubId: String,
    viewModel: ClubsViewModel = viewModel()
) {
    // Handle clubId "0" the same as blank or null
    if (clubId == "0") {
        LaunchedEffect(Unit) {
            navController.navigateUp()
        }
        return
    }
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var newMemberId by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
    
    val userRepository = remember { UserRepository.getInstance(context) }
    val statsRepository = remember { StatsRepository.getInstance(context) }
    
    val memberUsers = remember { mutableStateMapOf<String, UserDtoResponse?>() }
    val memberStats = remember { mutableStateMapOf<String, StatsResponse?>() }
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
            
            // Load stats for each member
            if (!memberStats.containsKey(memberId)) {
                coroutineScope.launch {
                    val result = statsRepository.getStats(memberId)
                    result.onSuccess { statsData ->
                        memberStats[memberId] = statsData
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
                title = { 
                    Text(
                        text = "Manage Club",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Empty action to balance the navigation icon
                    IconButton(onClick = { /* do nothing */ }) {
                        Box(modifier = Modifier.size(24.dp))
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
                    
                    // Only show add member section to the owner
                    if (club.ownerId == currentUserId) {
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
                                        label = { Text("Username") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    Button(
                                        onClick = {
                                            if (newMemberId.isNotBlank()) {
                                                viewModel.addMemberByUsername(context, clubId, newMemberId)
                                                newMemberId = ""
                                            }
                                        },
                                        enabled = newMemberId.isNotBlank()
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "Add")
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
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
                            ) {
                                items(club.members) { memberId ->
                                    val memberData = memberUsers[memberId]
                                    val memberStat = memberStats[memberId]
                                    MemberItem(
                                        memberId = memberId,
                                        userData = memberData,
                                        stats = memberStat,
                                        isOwner = club.ownerId == memberId,
                                        isLoading = !memberUsers.containsKey(memberId),
                                        onRemove = {
                                            viewModel.removeMember(context, clubId, memberId)
                                        },
                                        isCurrentUserOwner = club.ownerId == currentUserId,
                                        isCurrentUser = memberId == currentUserId,
                                        onUserClick = { userId ->
                                            // Navigate to user profile
                                            navController.navigate(Routes.anotherPerson(userId))
                                        }
                                    )
                                    HorizontalDivider()
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
    stats: StatsResponse? = null,
    isOwner: Boolean,
    isLoading: Boolean,
    onRemove: () -> Unit,
    isCurrentUserOwner: Boolean = false,
    isCurrentUser: Boolean = false,
    onUserClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var userStats by remember { mutableStateOf(stats) }
    var isLoadingStats by remember { mutableStateOf(stats == null) }
    
    LaunchedEffect(memberId, stats) {
        if (stats == null) {
            isLoadingStats = true
            val statsRepository = StatsRepository.getInstance(context)
            val result = statsRepository.getStats(memberId)
            result.onSuccess { statsData ->
                userStats = statsData
            }
            isLoadingStats = false
        } else {
            userStats = stats
            isLoadingStats = false
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                // Display user information with click action
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onUserClick(memberId) }
                        .weight(1f)
                ) {
                    // User Avatar - using EmojiIcon instead of basic Box
                    val avatarResId = remember(userData.avatar_url) {
                        val resourceName = userData.avatar_url ?: "e_profile"
                        val id = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                        if (id != 0) id else R.drawable.e_profile
                    }
                    
                    EmojiIcon(
                        iconResId = avatarResId,
                        size = 40
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${userData.first_name} ${userData.last_name}",
                                fontWeight = FontWeight.Medium
                            )
                            
                            // Add a small "View Profile" icon/text to indicate this is clickable
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "View",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        
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
                
                // Show delete button only if:
                // 1. Current user is the owner and viewing a non-owner member
                // 2. Current user is viewing their own member item (to leave the club)
                val showDeleteButton = (isCurrentUserOwner && !isOwner) || (isCurrentUser && !isOwner)
                
                if (showDeleteButton) {
                    IconButton(
                        onClick = onRemove
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = if (isCurrentUser) "Leave club" else "Remove member",
                            tint = Color.Red.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                // User data not loaded yet, but show their ID
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onUserClick(memberId) }
                        .weight(1f)
                ) {
                    // Replace Box with EmojiIcon for consistency
                    EmojiIcon(
                        iconResId = R.drawable.e_profile,
                        size = 32
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "User ID: $memberId",
                                fontWeight = FontWeight.Medium
                            )
                            
                            // Add a small "View Profile" icon/text to indicate this is clickable
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "View",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        
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
                
                // Same logic for showing delete button
                val showDeleteButton = (isCurrentUserOwner && !isOwner) || (isCurrentUser && !isOwner)
                
                if (showDeleteButton) {
                    IconButton(
                        onClick = onRemove
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = if (isCurrentUser) "Leave club" else "Remove member",
                            tint = Color.Red.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        // Display user stats in InfoBlocks
        if (!isLoading && userData != null) {
            Spacer(modifier = Modifier.height(8.dp))
            
            if (isLoadingStats) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Loading stats...", fontSize = 12.sp, color = Color.Gray)
                }
            } else if (userStats != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Level - no progress bar
                    LevelStatItem(
                        value = userStats!!.level.toString()
                    )
                    
                    // Steps
                    CompactStatItem(
                        label = "Steps",
                        value = userStats!!.steps_count.toString(),
                        percentage = (userStats!!.steps_count * 100 / 10000).coerceIn(0, 100)
                    )
                    
                    // Water
                    CompactStatItem(
                        label = "Water",
                        value = userStats!!.water_count.toString(),
                        percentage = (userStats!!.water_count * 100 / 10).coerceIn(0, 100)
                    )
                    
                    // Workouts
                    CompactStatItem(
                        label = "Workout",
                        value = userStats!!.workouts_count.toString(),
                        percentage = (userStats!!.workouts_count * 100 / 2).coerceIn(0, 100)
                    )
                }
            } else {
                Text(
                    text = "Stats not available",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LevelStatItem(value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = "Lvl",
            fontSize = 10.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        // Adding a spacer with the same height as the progress bar
        // to keep alignment with other stats
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun CompactStatItem(
    label: String,
    value: String,
    percentage: Int
) {
    val percentageColor = if (percentage >= 50) Color(0xFF228D00) else Color(0xFFFF9800)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .background(Color.LightGray, RoundedCornerShape(2.dp))
        ) {
            if (percentage > 0) {
                Box(
                    modifier = Modifier
                        .width((36.dp * percentage / 100f))
                        .height(4.dp)
                        .background(percentageColor, RoundedCornerShape(2.dp))
                )
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