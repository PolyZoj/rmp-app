package com.ifmo.rmp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun EmojiIcon(iconResId: Int, size: Int = 24) {
    Image(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = Modifier.size(size.dp)
    )
}