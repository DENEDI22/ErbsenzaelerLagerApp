package com.example.team6mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.team6mobileapp.ui.BarcodeScannerScreen
import com.example.team6mobileapp.ui.ArtikelListScreen
import com.example.team6mobileapp.ui.theme.Team6MobileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Team6MobileAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "scanner") {
        composable("scanner") {
            BarcodeScannerScreen(onNavigateToList = {
                navController.navigate("list")
            })
        }
        composable("list") {
            ArtikelListScreen(onNavigateBack = {
                navController.popBackStack()
            })
        }
    }
}