package com.ifmo.rmp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun GoalBox(
    iconResId: Int,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            EmojiIcon(iconResId = iconResId)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 14.sp, fontFamily = LatoFont)
        }
    }
}
