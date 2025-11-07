package com.teamdrishty.cashguard.ui.screens

import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val cameraManager = remember { CameraXManager(context) }
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
                            CircularProgressIndicator()
                            Text("Analyzing currency...", modifier = Modifier.padding(top = 8.dp))
                        } else {
                            Text(
                                "Point camera at banknote\nHold steady for automatic detection",
                                textAlign = TextAlign.Center
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