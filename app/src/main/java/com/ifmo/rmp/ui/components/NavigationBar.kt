package com.ifmo.rmp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.ifmo.rmp.ui.theme.RmpTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.ifmo.rmp.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.ifmo.rmp.ui.theme.LatoFont


// дописать, когда появится рабочий роутинг
@Composable
fun NavigationBar() {
    val items = listOf(
        "Home" to R.drawable.e_home,
        "Activities" to R.drawable.e_activities,
        "Clubs" to R.drawable.e_clubs,
        "Rewards" to R.drawable.e_rewards,
        "Profile" to R.drawable.e_profile
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        items.forEach { (route, emoji) ->
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        EmojiIcon(iconResId = emoji)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = route,
                            fontFamily = LatoFont,
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    }
                },
                selected = route == "Home",
                onClick = {  },
            )
        }
    }
}

// скорее всего нужно будет вынести в отдельный компонент,
// тк эмодзи используются не только тут
@Composable
fun EmojiIcon(iconResId: Int) {
    Image(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    RmpTheme {
        NavigationBar()
    }
}