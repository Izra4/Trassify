package com.praktikum.trassify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.praktikum.trassify.ui.theme.TrassifyTheme
import com.praktikum.trassify.view.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrassifyTheme {
                Surface (modifier = Modifier.fillMaxSize()){
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController = rememberNavController()){
    NavHost(
        navController = navController as NavHostController,
        startDestination = "welcome"
    ) {
        composable("welcome"){
            WelcomeScreen(navController = navController)
        }
    }
}