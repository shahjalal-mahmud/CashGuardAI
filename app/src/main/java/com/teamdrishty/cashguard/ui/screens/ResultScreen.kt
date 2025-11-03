package com.teamdrishty.cashguard.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ResultScreen(navController: NavController, isAuthentic: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isAuthentic) "✅ AUTHENTIC CURRENCY" else "❌ COUNTERFEIT DETECTED",
            color = if (isAuthentic) Color.Green else Color.Red,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add more result details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isAuthentic) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (isAuthentic) "Security Features Verified:" else "Security Issues Found:",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isAuthentic)
                        "• Watermark: ✓\n• Security Thread: ✓\n• Color-shifting Ink: ✓\n• Microprinting: ✓"
                    else
                        "• Watermark: ✗\n• Security Thread: ✗\n• Print Quality: Poor\n• Colors: Mismatched"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("scan") }
        ) {
            Text("Scan Another Note")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack("home", false) }
        ) {
            Text("Go Home")
        }
    }
}