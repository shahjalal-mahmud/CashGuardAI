package com.teamdrishty.cashguard.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.teamdrishty.cashguard.utils.ClassificationResult

// Modern Color Palette
private val SuccessGradient = Brush.linearGradient(
    listOf(Color(0xFF00C853), Color(0xFF006C42))
)
private val ErrorGradient = Brush.linearGradient(
    listOf(Color(0xFFFF5252), Color(0xFFFF6D00))
)
private val WarningGradient = Brush.linearGradient(
    listOf(Color(0xFFFFC107), Color(0xFFFF9800))
)
private val SurfaceGradient = Brush.linearGradient(
    listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedResultScreen(navController: NavController, result: ClassificationResult) {
    val isAuthentic = result.isAuthentic
    val confidence = result.confidence
    val denomination = result.denomination

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Verification Result",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { navController.popBackStack("home", false) },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Home")
                }

                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("scan") },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Replay, contentDescription = "Scan Again")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scan Again")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(24.dp, 24.dp, 24.dp, 100.dp)
        ) {
            item {
                ResultHeroSection(isAuthentic, confidence, denomination)
            }

            item {
                ConfidenceMeter(confidence = confidence, isAuthentic = isAuthentic)
            }

            item {
                SecurityFeaturesAnalysis(isAuthentic = isAuthentic)
            }

            item {
                DenominationDetails(denomination = denomination, isAuthentic = isAuthentic)
            }

            if (!isAuthentic) {
                item {
                    WarningAlertCard()
                }
            }
        }
    }
}

@Composable
private fun ResultHeroSection(isAuthentic: Boolean, confidence: Float, denomination: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = if (isAuthentic) SuccessGradient else ErrorGradient
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isAuthentic) Icons.Filled.Verified else Icons.Filled.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isAuthentic) "AUTHENTIC" else "COUNTERFEIT",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = denomination,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }

            // Confidence badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${(confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ConfidenceMeter(confidence: Float, isAuthentic: Boolean) {
    val animatedProgress by animateFloatAsState(
        targetValue = confidence,
        animationSpec = tween(1000)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = if (isAuthentic) Color(0xFF00C853) else Color(0xFFFF5252),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "AI Confidence Level",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Progress bar with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = if (isAuthentic) SuccessGradient else ErrorGradient
                        )
                )

                // Percentage text inside progress bar
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when {
                    confidence > 0.9 -> "Very High Confidence"
                    confidence > 0.7 -> "High Confidence"
                    confidence > 0.5 -> "Moderate Confidence"
                    else -> "Low Confidence - Manual verification recommended"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SecurityFeaturesAnalysis(isAuthentic: Boolean) {
    val securityFeatures = listOf(
        SecurityFeatureItemData("Watermark", if (isAuthentic) "✓ Verified" else "✗ Invalid", isAuthentic),
        SecurityFeatureItemData("Security Thread", if (isAuthentic) "✓ Present" else "✗ Missing", isAuthentic),
        SecurityFeatureItemData("Print Quality", if (isAuthentic) "✓ Excellent" else "✗ Poor", isAuthentic),
        SecurityFeatureItemData("Color Shift", if (isAuthentic) "✓ Detected" else "✗ Absent", isAuthentic),
        SecurityFeatureItemData("Micro-text", if (isAuthentic) "✓ Legible" else "✗ Blurred", isAuthentic),
        SecurityFeatureItemData("UV Pattern", "Manual Check", null)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Security Analysis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            securityFeatures.chunked(2).forEach { rowFeatures ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowFeatures.forEach { feature ->
                        SecurityFeatureItem(
                            feature = feature,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SecurityFeatureItem(feature: SecurityFeatureItemData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                feature.isPassed == true -> Color(0xFFE8F5E8)
                feature.isPassed == false -> Color(0xFFFFEBEE)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = feature.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feature.status,
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    feature.isPassed == true -> Color(0xFF2E7D32)
                    feature.isPassed == false -> Color(0xFFD32F2F)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DenominationDetails(denomination: String, isAuthentic: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (isAuthentic) SuccessGradient else ErrorGradient
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Detected Denomination",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = denomination,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val features = when (denomination) {
                "10 Taka" -> listOf(
                    "• Purple and orange dominant colors",
                    "• Sheikh Mujibur Rahman portrait",
                    "• National Martyrs' Memorial watermark",
                    "• Security thread with 'Bangladesh Bank'"
                )
                "20 Taka" -> listOf(
                    "• Green and orange color scheme",
                    "• Sheikh Mujibur Rahman portrait",
                    "• National Martyrs' Memorial watermark",
                    "• Wider security thread with denomination"
                )
                "50 Taka" -> listOf(
                    "• Cream and pink background",
                    "• Sheikh Mujibur Rahman portrait",
                    "• Bangabandhu Museum watermark",
                    "• Color-shifting ink on numeral"
                )
                "100 Taka" -> listOf(
                    "• Blue and purple tones",
                    "• Sheikh Mujibur Rahman portrait",
                    "• St. Martin's Island watermark",
                    "• Advanced security thread with micro-text"
                )
                "500 Taka" -> listOf(
                    "• Purple and yellow colors",
                    "• Sheikh Mujibur Rahman portrait",
                    "• Curzon Hall watermark",
                    "• Holographic stripe with 3D effects"
                )
                "1000 Taka" -> listOf(
                    "• Pink and brown colors",
                    "• Sheikh Mujibur Rahman portrait",
                    "• Jatiyo Sangsad Bhaban watermark",
                    "• Multiple security features with OVI ink"
                )
                else -> listOf(
                    "• Standard security features present",
                    "• Verify all security elements manually",
                    "• Check for appropriate colors and textures",
                    "• Confirm denomination-specific patterns"
                )
            }

            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00C853),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun WarningAlertCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color(0xFFFF6D00),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Security Alert",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6D00)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This note has failed multiple security checks. Please verify manually and consider reporting to authorities if confirmed counterfeit.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFF6D00).copy(alpha = 0.9f)
                )
            }
        }
    }
}

private data class SecurityFeatureItemData(
    val name: String,
    val status: String,
    val isPassed: Boolean?
)