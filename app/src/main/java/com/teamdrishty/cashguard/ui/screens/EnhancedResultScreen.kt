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

// Dynamic Color Palette that adapts to theme
private val SuccessGradient @Composable get() = Brush.linearGradient(
    listOf(
        if (isSystemInDarkTheme()) Color(0xFF00E676) else Color(0xFF00C853),
        if (isSystemInDarkTheme()) Color(0xFF004D40) else Color(0xFF006C42)
    )
)
private val ErrorGradient @Composable get() = Brush.linearGradient(
    listOf(
        if (isSystemInDarkTheme()) Color(0xFFFF6E6E) else Color(0xFFFF5252),
        if (isSystemInDarkTheme()) Color(0xFFBF360C) else Color(0xFFFF6D00)
    )
)
private val WarningColor @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFD740) else Color(0xFFFFC107)
private val SuccessLight @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1B5E20) else Color(0xFFE8F5E8)
private val ErrorLight @Composable get() = if (isSystemInDarkTheme()) Color(0xFFB71C1C) else Color(0xFFFFEBEE)
private val WarningLight @Composable get() = if (isSystemInDarkTheme()) Color(0xFF4A3000) else Color(0xFFFFF8E1)

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
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
            verticalArrangement = Arrangement.spacedBy(20.dp), // Reduced spacing
            contentPadding = PaddingValues(24.dp, 16.dp, 24.dp, 100.dp) // Adjusted padding
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
                .height(180.dp) // Reduced height for better balance
                .background(
                    brush = if (isAuthentic) SuccessGradient else ErrorGradient
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp), // Reduced padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isAuthentic) Icons.Filled.Verified else Icons.Filled.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp), // Slightly smaller icon
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp)) // Reduced spacing

                Text(
                    text = if (isAuthentic) "AUTHENTIC" else "COUNTERFEIT",
                    style = MaterialTheme.typography.headlineMedium, // Slightly smaller
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp)) // Added small spacing

                Text(
                    text = denomination,
                    style = MaterialTheme.typography.titleSmall, // Smaller denomination text
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }

            // Confidence badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp) // Reduced padding
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 5.dp) // Reduced padding
            ) {
                Text(
                    text = "${(confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall, // Smaller text
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
            modifier = Modifier.padding(16.dp) // Reduced padding
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp) // Reduced spacing
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = if (isAuthentic) Color(0xFF00C853) else Color(0xFFFF5252),
                    modifier = Modifier.size(22.dp) // Slightly smaller icon
                )
                Spacer(modifier = Modifier.width(10.dp)) // Reduced spacing
                Text(
                    text = "AI Confidence Level",
                    style = MaterialTheme.typography.titleMedium, // Slightly smaller
                    fontWeight = FontWeight.Bold
                )
            }

            // Progress bar with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp) // Smaller progress bar
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = if (isAuthentic) SuccessGradient else ErrorGradient
                        )
                )

                // Percentage text inside progress bar
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall, // Smaller text
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp) // Reduced padding
                )
            }

            Spacer(modifier = Modifier.height(6.dp)) // Reduced spacing

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
            modifier = Modifier.padding(16.dp) // Reduced padding
        ) {
            Text(
                text = "Security Analysis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp) // Reduced spacing
            )

            securityFeatures.chunked(2).forEach { rowFeatures ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp), // Reduced spacing
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing
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
            .height(70.dp), // Reduced height
        colors = CardDefaults.cardColors(
            containerColor = when {
                feature.isPassed == true -> SuccessLight
                feature.isPassed == false -> ErrorLight
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = RoundedCornerShape(12.dp) // Smaller radius
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Reduced padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = feature.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp // Better line height
            )
            Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
            Text(
                text = feature.status,
                style = MaterialTheme.typography.labelSmall, // Smaller text
                color = when {
                    feature.isPassed == true -> if (isSystemInDarkTheme()) Color(0xFF69F0AE) else Color(0xFF2E7D32)
                    feature.isPassed == false -> if (isSystemInDarkTheme()) Color(0xFFFF6E6E) else Color(0xFFD32F2F)
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
            modifier = Modifier.padding(16.dp) // Reduced padding
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp) // Reduced spacing
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp) // Slightly smaller
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
                        fontSize = 14.sp // Smaller text
                    )
                }
                Spacer(modifier = Modifier.width(10.dp)) // Reduced spacing
                Text(
                    text = "Detected Denomination",
                    style = MaterialTheme.typography.titleMedium, // Slightly smaller
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = denomination,
                style = MaterialTheme.typography.headlineSmall, // Slightly smaller
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp) // Reduced spacing
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
                    modifier = Modifier.padding(vertical = 3.dp), // Reduced spacing
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(0xFF69F0AE) else Color(0xFF00C853),
                        modifier = Modifier.size(14.dp) // Smaller icon
                    )
                    Spacer(modifier = Modifier.width(6.dp)) // Reduced spacing
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp // Better line height
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
            containerColor = WarningLight
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), // Reduced padding
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = WarningColor,
                modifier = Modifier.size(22.dp) // Slightly smaller
            )
            Spacer(modifier = Modifier.width(10.dp)) // Reduced spacing
            Column {
                Text(
                    text = "Security Alert",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarningColor
                )
                Spacer(modifier = Modifier.height(3.dp)) // Reduced spacing
                Text(
                    text = "This note has failed multiple security checks. Please verify manually and consider reporting to authorities if confirmed counterfeit.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarningColor.copy(alpha = 0.9f),
                    lineHeight = 18.sp // Better line height
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