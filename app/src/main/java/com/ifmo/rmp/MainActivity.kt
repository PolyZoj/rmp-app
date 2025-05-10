package com.ifmo.rmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ifmo.rmp.ui.navigation.AppNavGraph
import com.ifmo.rmp.ui.theme.RmpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RmpTheme {
                AppNavGraph("login")
            }
        }
    }
}
