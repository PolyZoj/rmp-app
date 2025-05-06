package com.ifmo.rmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.ifmo.rmp.data.auth.AuthManager
import com.ifmo.rmp.ui.navigation.AppNavGraph
import com.ifmo.rmp.ui.navigation.Routes
import com.ifmo.rmp.ui.theme.RmpTheme

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthManager.getInstance(this)
        enableEdgeToEdge()
        setContent {
            RmpTheme {
                MainScreen(authManager)
            }
        }
    }
}

@Composable
fun MainScreen(authManager: AuthManager) {
    val navController = rememberNavController()
    val isAuthenticated by authManager.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        val isValid = authManager.validateToken()
        if (!isValid) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.HOME) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    AppNavGraph(navController = navController)
}
