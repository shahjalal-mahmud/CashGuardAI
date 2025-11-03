package com.teamdrishty.cashguard.ui.screens

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.teamdrishty.cashguard.analysis.CurrencyAnalyzer
import com.teamdrishty.cashguard.utils.CameraXManager
import com.teamdrishty.cashguard.utils.RequestCameraPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val cameraManager = remember { CameraXManager(context) }
    val currencyAnalyzer = remember { CurrencyAnalyzer() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Currency") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                // Request camera permission
                RequestCameraPermission(
                    onPermissionGranted = { hasCameraPermission = true },
                    onPermissionDenied = {
                        navController.popBackStack()
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center  // FIXED: Use Arrangement.Center instead of Alignment.Center
                ) {
                    Text("Requesting camera permission...")
                    CircularProgressIndicator()
                }
            } else {
                // Show camera preview
                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER

                            // Start camera
                            cameraManager.startCamera(
                                previewView = this,
                                lifecycleOwner = lifecycleOwner,
                                coroutineScope = coroutineScope
                            ) { image ->
                                if (!isAnalyzing) {
                                    isAnalyzing = true

                                    coroutineScope.launch(Dispatchers.IO) {
                                        try {
                                            val result = currencyAnalyzer.analyzeImage(image)

                                            // Switch to main thread for navigation
                                            withContext(Dispatchers.Main) {
                                                navController.navigate("result/${result.isAuthentic}")
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            withContext(Dispatchers.Main) {
                                                navController.navigate("result/false")
                                            }
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

                // Scanning overlay
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,  // FIXED: Added proper vertical arrangement
                        modifier = Modifier.padding(bottom = 50.dp)
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator()
                            Text("Analyzing currency...", modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Text(
                                "Point camera at banknote\nHold steady for automatic detection",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
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