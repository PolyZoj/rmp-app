package com.ifmo.rmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoBlock(
    title: String,
    value: String,
    percentage: Int,
) {
    val percentageColor = if (percentage >= 0) Color.Green else Color.Red
    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .width(113.dp)
            .height(88.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$percentage%",
                fontSize = 16.sp,
                color = percentageColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoBlockPreview() {
    InfoBlock(
        title = "Steps",
        value = "8000",
        percentage = 20
    )
}
