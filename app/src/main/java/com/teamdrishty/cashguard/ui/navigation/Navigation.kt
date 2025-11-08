package com.teamdrishty.cashguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.teamdrishty.cashguard.ui.screens.EnhancedResultScreen
import com.teamdrishty.cashguard.ui.screens.HomeScreen
import com.teamdrishty.cashguard.ui.screens.ScanScreen
import com.teamdrishty.cashguard.ui.screens.SplashScreen
import com.teamdrishty.cashguard.utils.ClassificationResult

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash" // Changed from "home" to "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("scan") {
            ScanScreen(navController = navController)
        }
        composable(
            route = "result/{resultJson}",
            arguments = listOf(navArgument("resultJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val resultJson = backStackEntry.arguments?.getString("resultJson")
            val result = Gson().fromJson(resultJson, ClassificationResult::class.java)
            EnhancedResultScreen(
                navController = navController,
                result = result
            )
        }
    }
}