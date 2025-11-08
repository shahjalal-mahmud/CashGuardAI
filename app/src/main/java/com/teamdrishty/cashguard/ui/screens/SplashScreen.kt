package com.teamdrishty.cashguard.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(0f) }
    val glow = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    // Animated gradient background shimmer
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing)
        ), label = ""
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        ),
        start = Offset(x = offset, y = offset / 2), // FIXED: Use Offset(x, y) function
        end = Offset(x = offset / 3, y = offset)    // FIXED: Use Offset(x, y) function
    )

    // Start animation sequence
    LaunchedEffect(Unit) {
        delay(300)
        scale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = 80f
            )
        )
        alpha.animateTo(1f, tween(800))
        glow.animateTo(1f, tween(1200))
        textAlpha.animateTo(1f, tween(1000))
        delay(2400)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        // AI particle background effect
        AIParticleBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glowing circle "AI Core" logo
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF00C853),
                                Color(0xFFB2FF59).copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(130.dp)) {
                    val center = Offset(x = size.width / 2, y = size.height / 2) // FIXED
                    drawCircle(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF00E676),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2
                        ),
                        center = center
                    )
                }

                Text(
                    text = "AI",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.alpha(alpha.value)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App name with futuristic glow
            Text(
                text = "CashGuard AI",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Detect. Secure. Trust.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}

@Composable
fun AIParticleBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(), // FIXED: Use PI from kotlin.math
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing)
        ), label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(x = size.width / 2f, y = size.height / 2f) // FIXED
        val particleCount = 60
        val radius = size.minDimension / 2.2f
        val baseColor = Color(0xFF00E676).copy(alpha = 0.08f)

        for (i in 0 until particleCount) {
            val angle = (i / particleCount.toFloat()) * (2 * PI.toFloat()) // FIXED
            val x = center.x + radius * sin(angle + phase)
            val y = center.y + radius * sin(angle * 2 - phase)
            drawCircle(
                color = baseColor,
                radius = 2f + (sin(phase + i) + 1) * 1.5f,
                center = Offset(x = x, y = y) // FIXED: Use Offset(x, y) function
            )
        }
    }
}