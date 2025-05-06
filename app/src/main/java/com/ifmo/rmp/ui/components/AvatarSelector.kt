package com.ifmo.rmp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R

@Composable
fun AvatarSelector(
    selectedAvatar: String,
    onAvatarSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Choose Avatar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (avatarId in 1..4) {
                val avatarName = "e_avatar_$avatarId"
                AvatarItem(
                    avatarName = avatarName,
                    isSelected = selectedAvatar == avatarName,
                    onSelect = { onAvatarSelected(avatarName) }
                )
            }
        }
    }
}

@Composable
private fun AvatarItem(
    avatarName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    
    val borderWidth = if (isSelected) 3.dp else 1.dp
    
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = getAvatarResourceId(avatarName)),
            contentDescription = "Avatar $avatarName",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

private fun getAvatarResourceId(avatarName: String): Int {
    return when (avatarName) {
        "e_avatar_1" -> R.drawable.e_avatar_1
        "e_avatar_2" -> R.drawable.e_avatar_2
        "e_avatar_3" -> R.drawable.e_avatar_3
        "e_avatar_4" -> R.drawable.e_avatar_4
        else -> R.drawable.e_avatar_1
    }
} 