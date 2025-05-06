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
    selectedAvatar: Int,
    onAvatarSelected: (Int) -> Unit,
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
                AvatarItem(
                    avatarId = avatarId,
                    isSelected = selectedAvatar == avatarId,
                    onSelect = { onAvatarSelected(avatarId) }
                )
            }
        }
    }
}

@Composable
private fun AvatarItem(
    avatarId: Int,
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
            painter = painterResource(id = getAvatarResourceId(avatarId)),
            contentDescription = "Avatar $avatarId",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

private fun getAvatarResourceId(avatarId: Int): Int {
    return when (avatarId) {
        1 -> R.drawable.avatar_1
        2 -> R.drawable.avatar_2
        3 -> R.drawable.avatar_3
        4 -> R.drawable.avatar_4
        else -> R.drawable.avatar_1
    }
} 