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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun ClubsScreen(
    navController: NavController,
    viewModel: ClubsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Load clubs list when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.getClubsList(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Current Club Section
        Text(
            text = "Your current club",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display current club if available
        val currentClub = uiState.clubInfo
        if (currentClub != null) {
            ClubItem(club = currentClub, onClick = {})
        } else {
            // Placeholder for when no club is joined
            Text(
                text = "You haven't joined any club yet",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

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
        SearchBar(
            hint = "Search for clubs...",
            onSearchClick = { searchQuery ->
                // Handle search
            }
        )
        
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
                    ClubItem(
                        club = club,
                        onClick = {
                            // Navigate to club details or join
                            viewModel.getClubInfo(context, club.id)
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
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Club Avatar (Circle with icon)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCCCC)), // Light red background
            contentAlignment = Alignment.Center
        ) {
            // Using EmojiIcon for the club avatar
            EmojiIcon(
                iconResId = R.drawable.e_clubs,
                size = 32
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Club info
        Column {
            Text(
                text = club.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = club.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
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
