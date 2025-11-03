package com.teamdrishty.cashguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teamdrishty.cashguard.ui.screens.EnhancedResultScreen
import com.teamdrishty.cashguard.ui.screens.HomeScreen
import com.teamdrishty.cashguard.ui.screens.ScanScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("scan") {
            ScanScreen(navController = navController)
        }
        composable("result/{isAuthentic}") { backStackEntry ->
            val isAuthentic = backStackEntry.arguments?.getString("isAuthentic")?.toBoolean() ?: false
            EnhancedResultScreen(
                navController = navController,
                isAuthentic = isAuthentic
            )
        }
    }
}