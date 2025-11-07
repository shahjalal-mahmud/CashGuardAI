package com.teamdrishty.cashguard.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.teamdrishty.cashguard.analysis.TFLiteCurrencyClassifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class ClassificationResult(
    val isAuthentic: Boolean,
    val denomination: String,
    val confidence: Float
)

class CameraXManager(private val context: Context) {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var imageAnalyzer: ImageAnalysis? = null
    private val classifier = TFLiteCurrencyClassifier(context)

    // Add these variables for better control
    private var isAnalyzing = false
    private var lastAnalysisTime = 0L
    private val ANALYSIS_INTERVAL = 1500L // Analyze every 1.5 seconds
    private val CONFIDENCE_THRESHOLD = 0.8f // Only accept results with 80%+ confidence
    private var consecutiveHighConfidenceCount = 0
    private val REQUIRED_CONSECUTIVE_DETECTIONS = 2 // Require 2 consecutive high-confidence detections

    fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        onImageClassified: (ClassificationResult) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview setup
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // Image Analyzer for TFLite classification
            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { image ->
                        val currentTime = System.currentTimeMillis()

                        // Only analyze if enough time has passed since last analysis
                        if (!isAnalyzing && (currentTime - lastAnalysisTime) > ANALYSIS_INTERVAL) {
                            isAnalyzing = true
                            lastAnalysisTime = currentTime

                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    val bitmap = image.toBitmap()
                                    val (label, confidence) = classifier.classify(bitmap)

                                    Log.d("CameraXManager", "Classification: '$label', Confidence: $confidence")

                                    // Only proceed if confidence is high enough
                                    if (confidence >= CONFIDENCE_THRESHOLD) {
                                        consecutiveHighConfidenceCount++
                                        Log.d("CameraXManager", "High confidence detection #$consecutiveHighConfidenceCount")

                                        // Only trigger result after multiple consistent detections
                                        if (consecutiveHighConfidenceCount >= REQUIRED_CONSECUTIVE_DETECTIONS) {
                                            // Enhanced logic for your specific labels
                                            val isAuthentic = when {
                                                label.contains("real", ignoreCase = true) -> true
                                                label.contains("Real 200 Notes", ignoreCase = true) -> true
                                                else -> false
                                            }

                                            val denomination = "200 Taka"

                                            Log.d("CameraXManager", "Triggering result: Authentic=$isAuthentic")

                                            onImageClassified(
                                                ClassificationResult(
                                                    isAuthentic = isAuthentic,
                                                    denomination = denomination,
                                                    confidence = confidence
                                                )
                                            )
                                            // Reset counter after successful detection
                                            consecutiveHighConfidenceCount = 0
                                        }
                                    } else {
                                        Log.d("CameraXManager", "Low confidence: $confidence, resetting counter")
                                        consecutiveHighConfidenceCount = 0 // Reset counter on low confidence
                                    }
                                } catch (e: Exception) {
                                    Log.e("CameraXManager", "Image classification failed", e)
                                    consecutiveHighConfidenceCount = 0
                                } finally {
                                    image.close()
                                    isAnalyzing = false
                                }
                            }
                        } else {
                            // Skip this frame
                            image.close()
                        }
                    }
                }

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("CameraXManager", "Camera binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun shutdown() {
        cameraExecutor.shutdown()
    }

    // Extension function to convert ImageProxy to Bitmap
    private fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}