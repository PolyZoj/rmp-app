package com.ifmo.rmp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.ui.theme.LatoFont

@Composable
fun BigInfoBlock(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(68.dp)
            .width(180.dp),
        border = BorderStroke(1.dp, Color(0xFF6B6A6A).copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFF6B6A6A).copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontFamily = LatoFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = LatoFont
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BigInfoBlockPreview() {
    BigInfoBlock(
        title = "Steps taken",
        value = "10000 / 20000"
    )
}