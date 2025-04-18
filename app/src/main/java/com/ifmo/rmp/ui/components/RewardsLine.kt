package com.ifmo.rmp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R

@Composable
fun RewardsLine(
    iconResId: Int,
    title: String,
    subtitle: String,
    status: String, // тут In Progres или Completed
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                EmojiIcon(iconResId, size = 28)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = status,
                fontSize = 12.sp,
                color = if (status == "Completed") Color(0xFF228D00) else Color(0xFF8B0000),
                fontWeight = FontWeight.Medium
            )
        }
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsLinePreview() {
    Column {
        RewardsLine(
            iconResId = R.drawable.e_trophy,
            title = "10K Steps",
            subtitle = "Daily Challenge",
            status = "In Progress"
        )
        RewardsLine(
            iconResId = R.drawable.e_trophy,
            title = "Water Master",
            subtitle = "Drink 2L water for 7 days",
            status = "Completed"
        )
    }
}
