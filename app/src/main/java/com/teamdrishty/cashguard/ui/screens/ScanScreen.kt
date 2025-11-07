package com.teamdrishty.cashguard.ui.screens

import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.teamdrishty.cashguard.utils.CameraXManager
import com.teamdrishty.cashguard.utils.ClassificationResult
import com.teamdrishty.cashguard.utils.RequestCameraPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(true) }

    val cameraManager = remember { CameraXManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Reset when entering screen
    DisposableEffect(Unit) {
        onDispose {
            // Clean up if needed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan 200 Taka Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Stop camera properly when going back
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasCameraPermission) {
                RequestCameraPermission(
                    onPermissionGranted = { hasCameraPermission = true },
                    onPermissionDenied = { navController.popBackStack() }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Requesting camera permission...")
                    CircularProgressIndicator()
                }
            } else {
                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER

                            cameraManager.startCamera(
                                previewView = this,
                                lifecycleOwner = lifecycleOwner,
                                coroutineScope = coroutineScope
                            ) { result: ClassificationResult ->
                                if (!isAnalyzing) {
                                    isAnalyzing = true

                                    coroutineScope.launch(Dispatchers.Main) {
                                        try {
                                            // Show analyzing state for longer
                                            delay(2000L)

                                            // Serialize the ClassificationResult to JSON
                                            val resultJson = Gson().toJson(result)
                                            // Navigate with JSON string
                                            navController.navigate("result/$resultJson")
                                        } catch (e: Exception) {
                                            Log.e("ScanScreen", "Navigation failed", e)
                                        } finally {
                                            isAnalyzing = false
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay for scanning instructions
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 50.dp)
                    ) {
                        if (isAnalyzing) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Text("Analyzing currency...", modifier = Modifier.padding(top = 8.dp))
                                Text(
                                    "Please wait",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Point camera at 200 Taka note",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Hold steady for automatic detection\n" +
                                            "Ensure good lighting and clear view\n" +
                                            "Fill the frame with the banknote",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Add a manual capture button as alternative
                                Button(
                                    onClick = {
                                        // You can add manual capture logic here
                                        isAnalyzing = true
                                        coroutineScope.launch {
                                            delay(3000L) // Simulate analysis
                                            // For now, just show it's working
                                            isAnalyzing = false
                                        }
                                    },
                                    enabled = !isAnalyzing
                                ) {
                                    Text("Force Check Current Frame")
                                }
                            }
                        }
                    }
                }

                // Scanning frame overlay
                if (!isAnalyzing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val frameWidth = width * 0.8f
                            val frameHeight = frameWidth * 0.5f // Banknote aspect ratio

                            // Draw scanning frame
                            drawRect(
                                color = Color.Green.copy(alpha = 0.3f),
                                topLeft = Offset((width - frameWidth) / 2, (height - frameHeight) / 2),
                                size = Size(frameWidth, frameHeight),
                                style = Stroke(width = 4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberCoroutineScope(): CoroutineScope {
    return remember {
        CoroutineScope(Dispatchers.Main + Job())
    }
}