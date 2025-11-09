package com.teamdrishty.cashguard.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Color scheme for light and dark modes
data class SplashColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val particleColor: Color,
    val geometricPatternColor: Color,
    val glowColor: Color
)

@Composable
fun getSplashColors(isDarkMode: Boolean): SplashColors {
    return if (isDarkMode) {
        // Dark mode colors - sophisticated deep blues with vibrant AI green
        SplashColors(
            primary = Color(0xFF00E676),
            primaryVariant = Color(0xFF00C853),
            secondary = Color(0xFFB2FF59),
            background = Color(0xFF0A0F1C),
            surface = Color(0xFF1A2A4F),
            textPrimary = Color.White,
            textSecondary = Color.White.copy(alpha = 0.8f),
            particleColor = Color(0xFF00E676),
            geometricPatternColor = Color(0xFF00E676),
            glowColor = Color(0xFF00E676)
        )
    } else {
        // Light mode colors - clean whites with professional blue accent
        SplashColors(
            primary = Color(0xFF0066CC),
            primaryVariant = Color(0xFF004499),
            secondary = Color(0xFF3399FF),
            background = Color(0xFFF8FAFF),
            surface = Color(0xFFE3F2FD),
            textPrimary = Color(0xFF1A237E),
            textSecondary = Color(0xFF283593).copy(alpha = 0.8f),
            particleColor = Color(0xFF0066CC),
            geometricPatternColor = Color(0xFF0066CC),
            glowColor = Color(0xFF0066CC)
        )
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // Better dark mode detection
    val isDarkMode = MaterialTheme.colorScheme.primary == Color(0xFF00E676) ||
            isColorDark(MaterialTheme.colorScheme.background)

    val colors = getSplashColors(isDarkMode)

    // Animation states
    val logoScale = remember { Animatable(0.8f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val backgroundReveal = remember { Animatable(0f) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Rotating gradient angle
    val gradientAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "gradientRotation"
    )

    // Pulsing glow effect
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    // Start animation sequence
    LaunchedEffect(Unit) {
        // Initial delay
        delay(200)

        // Background reveal
        backgroundReveal.animateTo(1f, tween(800))

        // Logo entrance
        logoScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        logoAlpha.animateTo(1f, tween(600))

        // Text entrance
        delay(400)
        textAlpha.animateTo(1f, tween(800))

        // Navigate after display
        delay(1800)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        // Animated geometric background with theme colors
        GeometricPatternBackground(
            phase = gradientAngle / 360f,
            colors = colors,
            isDarkMode = isDarkMode
        )

        // Central glow effect with theme colors
        CentralGlowEffect(pulse = pulse, colors = colors, isDarkMode = isDarkMode)

        // Floating particles with theme colors
        FloatingParticles(colors = colors, isDarkMode = isDarkMode)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // FIXED: Changed from Alignment.Center to Arrangement.Center
            modifier = Modifier.graphicsLayer {
                alpha = backgroundReveal.value
            }
        ) {
            // Modern AI core logo with multiple layers and theme colors
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale.value)
            ) {
                // Outer ring with rotation
                Canvas(
                    modifier = Modifier
                        .size(140.dp)
                        .rotate(gradientAngle)
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.primary.copy(alpha = if (isDarkMode) 0.3f else 0.2f),
                                colors.primaryVariant.copy(alpha = if (isDarkMode) 0.1f else 0.05f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2
                        ),
                        center = center,
                        radius = size.width / 2
                    )
                }

                // Middle ring with pulse
                Canvas(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(pulse)
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.primary.copy(alpha = if (isDarkMode) 0.4f else 0.3f),
                                colors.secondary.copy(alpha = if (isDarkMode) 0.2f else 0.1f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2
                        ),
                        center = center,
                        radius = size.width / 2
                    )
                }

                // Inner core with theme colors
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (isDarkMode) {
                                    listOf(
                                        colors.primary,
                                        colors.primaryVariant,
                                        Color(0xFF009624)
                                    )
                                } else {
                                    listOf(
                                        colors.primary,
                                        colors.primaryVariant,
                                        colors.secondary
                                    )
                                }
                            )
                        )
                        .graphicsLayer {
                            alpha = logoAlpha.value
                            rotationZ = gradientAngle / 2
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cg",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = -gradientAngle / 2
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name with modern typography and theme colors
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    alpha = textAlpha.value
                }
            ) {
                Text(
                    text = "CASHGUARD",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = colors.textPrimary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "AI SECURITY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Minimal tagline with fade-in effect and theme colors
                Text(
                    text = "Intelligent Financial Protection",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier.graphicsLayer {
                        alpha = textAlpha.value
                    }
                )
            }
        }
    }
}

@Composable
fun GeometricPatternBackground(phase: Float, colors: SplashColors, isDarkMode: Boolean) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(8.dp)
    ) {
        val patternSize = 100f
        val rows = (size.height / patternSize).toInt() + 2
        val cols = (size.width / patternSize).toInt() + 2

        for (i in 0..rows) {
            for (j in 0..cols) {
                val x = j * patternSize
                val y = i * patternSize

                rotate(degrees = phase * 360f) {
                    drawRect(
                        color = colors.geometricPatternColor.copy(
                            alpha = if (isDarkMode) 0.03f else 0.02f
                        ),
                        topLeft = Offset(x, y),
                        size = Size(patternSize * 0.3f, patternSize * 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun CentralGlowEffect(pulse: Float, colors: SplashColors, isDarkMode: Boolean) {
    Canvas(
        modifier = Modifier
            .size(300.dp)
            .graphicsLayer {
                alpha = if (isDarkMode) 0.3f else 0.15f
            }
    ) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    colors.glowColor.copy(
                        alpha = if (isDarkMode) 0.2f else 0.1f
                    ),
                    colors.glowColor.copy(
                        alpha = if (isDarkMode) 0.1f else 0.05f
                    ),
                    Color.Transparent
                ),
                center = center,
                radius = size.width / 2 * pulse
            ),
            center = center,
            radius = size.width / 2 * pulse
        )
    }
}

@Composable
fun FloatingParticles(colors: SplashColors, isDarkMode: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val floatAnimation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(), // FIXED: Added parentheses for proper conversion
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing)
        ), label = "particles1"
    )

    val floatAnimation2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(), // FIXED: Added parentheses for proper conversion
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing)
        ), label = "particles2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = center
        val particleCount = 12

        // First ring of particles
        for (i in 0 until particleCount) {
            val angle = (i / particleCount.toFloat()) * (2 * PI.toFloat()) + floatAnimation1
            val radius = size.minDimension * 0.4f
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)

            drawCircle(
                color = colors.particleColor.copy(
                    alpha = if (isDarkMode) 0.15f else 0.08f
                ),
                radius = 3f + sin(floatAnimation1 + i) * 2f,
                center = Offset(x, y)
            )
        }

        // Second ring of particles
        for (i in 0 until particleCount) {
            val angle = (i / particleCount.toFloat()) * (2 * PI.toFloat()) + floatAnimation2
            val radius = size.minDimension * 0.6f
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)

            drawCircle(
                color = colors.secondary.copy(
                    alpha = if (isDarkMode) 0.1f else 0.05f
                ),
                radius = 2f + cos(floatAnimation2 + i) * 1.5f,
                center = Offset(x, y)
            )
        }
    }
}
fun isColorDark(color: Color): Boolean {
    // Convert color to grayscale using relative luminance formula
    val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
    return luminance < 0.5f
}