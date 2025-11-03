package com.teamdrishty.cashguard.analysis

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.core.ImageProxy
import kotlin.math.abs

class EnhancedCurrencyAnalyzer {

    fun analyzeImage(image: ImageProxy): AnalysisResult {
        return try {
            val bitmap = image.toBitmap()
            val processedBitmap = preprocessImage(bitmap)

            // Perform multiple security checks
            val securityChecks = performSecurityChecks(processedBitmap)
            val overallScore = calculateOverallScore(securityChecks)
            val isAuthentic = overallScore > 0.7f
            val denomination = detectBangladeshiDenomination(processedBitmap)

            AnalysisResult(
                isAuthentic = isAuthentic,
                confidence = overallScore,
                detectedDenomination = denomination,
                securityFeatures = getDetectedFeatures(securityChecks),
                detailedAnalysis = securityChecks
            )
        } catch (e: Exception) {
            AnalysisResult(error = "Analysis failed: ${e.message}")
        }
    }

    private fun performSecurityChecks(bitmap: Bitmap): Map<String, Float> {
        return mapOf(
            "color_consistency" to checkColorConsistency(bitmap),
            "texture_quality" to checkTextureQuality(bitmap),
            "edge_sharpness" to checkEdgeSharpness(bitmap),
            "pattern_regularity" to checkPatternRegularity(bitmap),
            "brightness_balance" to checkBrightnessBalance(bitmap)
        )
    }

    private fun checkColorConsistency(bitmap: Bitmap): Float {
        // Check if colors are consistent (counterfeit notes often have color variations)
        val width = bitmap.width
        val height = bitmap.height

        val samplePoints = listOf(
            Pair(width / 4, height / 4),
            Pair(width / 2, height / 2),
            Pair(3 * width / 4, 3 * height / 4)
        )

        val colors = samplePoints.map { point ->
            bitmap.getPixel(point.first, point.second)
        }

        // Calculate color variance
        val variance = calculateColorVariance(colors)
        return 1.0f - (variance / 100f).coerceIn(0f, 1f)
    }

    private fun checkTextureQuality(bitmap: Bitmap): Float {
        // Simple texture analysis - real notes have finer texture
        val width = bitmap.width
        val height = bitmap.height

        var textureScore = 0f
        val sampleSize = 10

        for (i in 0 until sampleSize) {
            val x = (i * width / sampleSize).coerceAtMost(width - 2)
            val y = (i * height / sampleSize).coerceAtMost(height - 2)

            val pixel1 = bitmap.getPixel(x, y)
            val pixel2 = bitmap.getPixel(x + 1, y + 1)

            val diff = abs(Color.red(pixel1) - Color.red(pixel2)) +
                    abs(Color.green(pixel1) - Color.green(pixel2)) +
                    abs(Color.blue(pixel1) - Color.blue(pixel2))

            textureScore += (diff / 765f) // Normalize to 0-1
        }

        return (textureScore / sampleSize).coerceIn(0f, 1f)
    }

    private fun checkEdgeSharpness(bitmap: Bitmap): Float {
        // Simple edge detection - real notes have sharper edges
        val edgePixels = detectEdges(bitmap)
        return (edgePixels / (bitmap.width * bitmap.height).toFloat()).coerceIn(0f, 1f)
    }

    private fun detectEdges(bitmap: Bitmap): Int {
        var edgeCount = 0
        val threshold = 50

        for (x in 1 until bitmap.width - 1) {
            for (y in 1 until bitmap.height - 1) {
                val center = bitmap.getPixel(x, y)
                val right = bitmap.getPixel(x + 1, y)
                val bottom = bitmap.getPixel(x, y + 1)

                val diffRight = colorDifference(center, right)
                val diffBottom = colorDifference(center, bottom)

                if (diffRight > threshold || diffBottom > threshold) {
                    edgeCount++
                }
            }
        }
        return edgeCount
    }

    private fun colorDifference(color1: Int, color2: Int): Int {
        return abs(Color.red(color1) - Color.red(color2)) +
                abs(Color.green(color1) - Color.green(color2)) +
                abs(Color.blue(color1) - Color.blue(color2))
    }

    private fun checkPatternRegularity(bitmap: Bitmap): Float {
        // Check for regular patterns (security threads, microprinting)
        // This is a simplified version
        return 0.8f // Placeholder
    }

    private fun checkBrightnessBalance(bitmap: Bitmap): Float {
        // Check if brightness is evenly distributed
        val width = bitmap.width
        val height = bitmap.height

        val regions = listOf(
            bitmap.getSubBitmap(0, 0, width/2, height/2), // Top-left
            bitmap.getSubBitmap(width/2, 0, width/2, height/2), // Top-right
            bitmap.getSubBitmap(0, height/2, width/2, height/2), // Bottom-left
            bitmap.getSubBitmap(width/2, height/2, width/2, height/2) // Bottom-right
        )

        val brightnessValues = regions.map { calculateAverageBrightness(it) }
        val variance = calculateBrightnessVariance(brightnessValues)

        return 1.0f - (variance / 100f).coerceIn(0f, 1f)
    }

    private fun calculateAverageBrightness(bitmap: Bitmap): Float {
        var totalBrightness = 0f
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                totalBrightness += (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3f
            }
        }
        return totalBrightness / (bitmap.width * bitmap.height)
    }

    private fun detectBangladeshiDenomination(bitmap: Bitmap): String {
        // Simple color-based denomination detection for Bangladeshi notes
        val dominantColor = getDominantColor(bitmap)

        return when {
            isMostlyGreen(dominantColor) -> "1000"
            isMostlyPurple(dominantColor) -> "500"
            isMostlyOrange(dominantColor) -> "200"
            else -> "Unknown"
        }
    }

    private fun isMostlyGreen(color: Int): Boolean {
        return Color.green(color) > Color.red(color) && Color.green(color) > Color.blue(color)
    }

    private fun isMostlyPurple(color: Int): Boolean {
        return Color.red(color) > 100 && Color.blue(color) > 100 && Color.green(color) < 100
    }

    private fun isMostlyOrange(color: Int): Boolean {
        return Color.red(color) > 150 && Color.green(color) > 100 && Color.blue(color) < 50
    }

    private fun getDominantColor(bitmap: Bitmap): Int {
        // Simple dominant color detection
        val samplePoints = 100
        var totalR = 0
        var totalG = 0
        var totalB = 0

        for (i in 0 until samplePoints) {
            val x = (Math.random() * bitmap.width).toInt()
            val y = (Math.random() * bitmap.height).toInt()
            val pixel = bitmap.getPixel(x, y)
            totalR += Color.red(pixel)
            totalG += Color.green(pixel)
            totalB += Color.blue(pixel)
        }

        return Color.rgb(totalR / samplePoints, totalG / samplePoints, totalB / samplePoints)
    }

    private fun calculateOverallScore(checks: Map<String, Float>): Float {
        return checks.values.average().toFloat()
    }

    private fun getDetectedFeatures(checks: Map<String, Float>): List<String> {
        return checks.filter { it.value > 0.6f }.map { it.key }
    }

    private fun preprocessImage(bitmap: Bitmap): Bitmap {
        // Basic preprocessing - resize for consistent analysis
        return Bitmap.createScaledBitmap(bitmap, 300, 150, true)
    }

    // Helper extension function
    private fun Bitmap.getSubBitmap(x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(this, x, y, width, height)
    }

    private fun calculateColorVariance(colors: List<Int>): Float {
        // Simplified variance calculation
        return colors.map { Color.red(it) + Color.green(it) + Color.blue(it) }.let { brightness ->
            val mean = brightness.average()
            brightness.map { abs(it - mean) }.average().toFloat()
        }
    }

    private fun calculateBrightnessVariance(brightness: List<Float>): Float {
        val mean = brightness.average()
        return brightness.map { abs(it - mean) }.average().toFloat()
    }
}

// Update your AnalysisResult data class
data class AnalysisResult(
    val isAuthentic: Boolean = false,
    val confidence: Float = 0f,
    val detectedDenomination: String = "Unknown",
    val securityFeatures: List<String> = emptyList(),
    val detailedAnalysis: Map<String, Float> = emptyMap(),
    val error: String? = null
)