package com.ifmo.rmp.ui.screens.clubs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ifmo.rmp.ui.components.BigButton
import com.ifmo.rmp.ui.components.CustomTextField

@Composable
fun ClubCreationScreen(
    navController: NavController,
    viewModel: ClubsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // If club creation is successful, navigate back to clubs screen
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigateUp()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Create a New Club",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Club name field
        CustomTextField(
            label = "Club Name",
            value = uiState.clubName,
            onValueChange = { viewModel.onClubNameChange(it) },
            placeholder = "Enter club name"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Club description field
        CustomTextField(
            label = "Description",
            value = uiState.clubDescription,
            onValueChange = { viewModel.onClubDescriptionChange(it) },
            placeholder = "Enter club description"
        )
        
        // Error message (if any)
        if (uiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Create button
        BigButton(
            text = if (uiState.isLoading) "Creating..." else "Create Club",
            onClick = { 
                if (!uiState.isLoading) {
                    viewModel.createClub(context)
                }
            }
        )
        
        // Cancel button
        Spacer(modifier = Modifier.height(16.dp))
        BigButton(
            text = "Cancel",
            onClick = { navController.navigateUp() }
        )
        
        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
} 