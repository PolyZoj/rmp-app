package com.ifmo.rmp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifmo.rmp.R

@Composable
fun BigEmojiButton(
    emojiResId: Int,
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    emojiSize: Int = 48,
    cornerRadius: Int = 8,
    outerPadding: Int = 4
) {
    val borderColor = Color(0xFF6B6A6A).copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .padding(outerPadding.dp)
            .size(100.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(cornerRadius.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmojiIcon(
                    iconResId = emojiResId,
                    size = emojiSize
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BigEmojiButtonPreview() {
    BigEmojiButton(
        emojiResId = R.drawable.e_trophy,
        text = "Награды")
}