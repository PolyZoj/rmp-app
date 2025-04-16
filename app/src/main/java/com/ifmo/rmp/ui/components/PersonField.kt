package com.ifmo.rmp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.ifmo.rmp.R

@Composable
fun PersonField(
    name: String,
    iconResId: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmojiIcon(iconResId = iconResId, size = 28)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonFieldPreview() {
    PersonField(name = "Тоха", iconResId = R.drawable.e_profile)
}

