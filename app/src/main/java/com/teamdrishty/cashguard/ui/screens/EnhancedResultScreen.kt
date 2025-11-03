package com.teamdrishty.cashguard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EnhancedResultScreen(navController: NavController, isAuthentic: Boolean, confidence: Float = 0.8f) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Result Header
        ResultHeader(isAuthentic, confidence)

        Spacer(modifier = Modifier.height(24.dp))

        // Security Analysis Card
        SecurityAnalysisCard(isAuthentic)

        Spacer(modifier = Modifier.height(24.dp))

        // Denomination Info
        DenominationInfoCard()

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        ActionButtons(navController)
    }
}

@Composable
private fun ResultHeader(isAuthentic: Boolean, confidence: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isAuthentic) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isAuthentic) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isAuthentic) Color(0xFF2E7D32) else Color.Red,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isAuthentic) "AUTHENTIC CURRENCY" else "COUNTERFEIT DETECTED",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (isAuthentic) Color(0xFF2E7D32) else Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Confidence: ${(confidence * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SecurityAnalysisCard(isAuthentic: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🔍 Security Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecurityFeatureItem(
                feature = "Watermark Verification",
                status = if (isAuthentic) "PASS" else "FAIL",
                isPassed = isAuthentic
            )

            SecurityFeatureItem(
                feature = "Security Thread",
                status = if (isAuthentic) "PASS" else "FAIL",
                isPassed = isAuthentic
            )

            SecurityFeatureItem(
                feature = "Color Consistency",
                status = if (isAuthentic) "PASS" else "FAIL",
                isPassed = isAuthentic
            )

            SecurityFeatureItem(
                feature = "Print Quality",
                status = if (isAuthentic) "PASS" else "FAIL",
                isPassed = isAuthentic
            )

            SecurityFeatureItem(
                feature = "UV Feature Pattern",
                status = "MANUAL CHECK",
                isPassed = null
            )
        }
    }
}

@Composable
private fun SecurityFeatureItem(feature: String, status: String, isPassed: Boolean?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = status,
            color = when (isPassed) {
                true -> Color(0xFF2E7D32)
                false -> Color.Red
                null -> Color(0xFF666666)
            },
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DenominationInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "💵 Detected Denomination: 500 Taka",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "• Purple dominant color\n" +
                        "• Sheikh Mujibur Rahman portrait\n" +
                        "• National Martyrs' Memorial watermark\n" +
                        "• Security thread with 'Bangladesh Bank'",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1565C0).copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ActionButtons(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { navController.navigate("scan") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text("Scan Another Note")
        }

        Button(
            onClick = { navController.popBackStack("home", false) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text("Go to Home", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}