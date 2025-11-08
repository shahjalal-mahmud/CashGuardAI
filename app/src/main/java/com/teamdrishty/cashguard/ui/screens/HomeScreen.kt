package com.teamdrishty.cashguard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Professional Color Palette (Deep Forest Green, aligning with finance/security)
private val PrimaryGreen = Color(0xFF006C42)
private val ContainerGreen = Color(0xFFC1F8C0)

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Using system background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp), // Increased vertical padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Title - Elevated Typography
            Text(
                text = "CashGuard AI 🛡️", // Added a subtle security emoji
                style = MaterialTheme.typography.displaySmall, // Larger, more impactful title
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = "Advanced Counterfeit Taka Detection",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 🚀 Hero/Action Card - The main professional touch
            ScanActionCard(navController)

            Spacer(modifier = Modifier.height(32.dp))

            // 💡 Informational Sections
            InstructionsSectionPro()

            Spacer(modifier = Modifier.height(24.dp))

            SecurityFeaturesSectionPro()

            Spacer(modifier = Modifier.height(24.dp)) // Extra space at bottom
        }
    }
}

@Composable
fun ScanActionCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp), // Set a fixed, prominent height
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Use a soft container color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // High elevation for prominence
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                contentDescription = "Scan Icon",
                tint = MaterialTheme.colorScheme.primary, // Use primary color for the icon
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("scan")
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Slightly less than full width for better padding effect
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen // Custom, strong action color
                )
            ) {
                Text(
                    text = "START BANKNOTE SCAN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun InstructionsSectionPro() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "How to Use", icon = Icons.Filled.Info)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = "1. Tap 'START BANKNOTE SCAN'\n" +
                        "2. Align the currency clearly within the camera guide\n" +
                        "3. Ensure bright, even lighting for best AI results\n" +
                        "4. Hold Steady for a moment while the AI analyzes",
                style = MaterialTheme.typography.bodyLarge, // Slightly larger body text
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SecurityFeaturesSectionPro() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Security Overview", icon = Icons.Filled.Security)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = ContainerGreen // A light, custom green for this section
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = "• Watermark: Bangladesh Bank emblem/portrait\n" +
                        "• Security Thread: Embedded silver line with text\n" +
                        "• OVI: Color-shifting ink on high-denomination notes\n" +
                        "• Tactile Print: Raised ink for a distinct touch texture",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                color = PrimaryGreen.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}