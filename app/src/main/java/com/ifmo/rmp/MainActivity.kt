package com.ifmo.rmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.Context
import com.ifmo.rmp.ui.navigation.AppNavGraph
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.RmpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val startDestination = if (isUserLoggedIn()) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }
        
        setContent {
            RmpTheme {
                AppNavGraph(startDestination)
            }
        }
    }
    
    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)?.isNotBlank() == true
    }
}
