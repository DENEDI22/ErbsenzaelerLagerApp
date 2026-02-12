package com.example.team6mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.team6mobileapp.model.Artikel
import com.example.team6mobileapp.ui.BarcodeScannerScreen
import com.example.team6mobileapp.ui.ArtikelListScreen
import com.example.team6mobileapp.ui.ArtikelDetailedView
import com.example.team6mobileapp.ui.ArtikelCreateScreen
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
    var selectedArtikel by remember { mutableStateOf<Artikel?>(null) }

    NavHost(navController = navController, startDestination = "scanner") {
        composable("scanner") {
            BarcodeScannerScreen(
                onNavigateToList = {
                    navController.navigate("list")
                },
                onNavigateToDetail = { artikel ->
                    selectedArtikel = artikel
                    navController.navigate("detail")
                },
                onNavigateToCreate = {
                    navController.navigate("create")
                }
            )
        }
        composable("list") {
            ArtikelListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = { artikel ->
                    selectedArtikel = artikel
                    navController.navigate("detail")
                }
            )
        }
        composable("detail") {
            selectedArtikel?.let { artikel ->
                ArtikelDetailedView(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    artikel = artikel
                )
            }
        }
        composable("create") {
            ArtikelCreateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}