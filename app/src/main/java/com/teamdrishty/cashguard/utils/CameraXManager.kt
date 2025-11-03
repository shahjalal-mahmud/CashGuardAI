package com.teamdrishty.cashguard.utils

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXManager(private val context: Context) {
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var imageAnalyzer: ImageAnalysis? = null

    fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        coroutineScope: CoroutineScope,
        onImageCaptured: (ImageProxy) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Image Analyzer for counterfeit detection
            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        // Use coroutine scope to handle the analysis
                        coroutineScope.launch(Dispatchers.IO) {
                            onImageCaptured(image)
                        }
                    }
                }

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("CameraXManager", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun shutdown() {
        cameraExecutor.shutdown()
    }
}