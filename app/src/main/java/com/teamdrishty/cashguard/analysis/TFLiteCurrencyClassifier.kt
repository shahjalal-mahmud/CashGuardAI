package com.teamdrishty.cashguard.analysis

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.BufferedReader
import java.io.InputStreamReader

class TFLiteCurrencyClassifier(context: Context) {

    private val classifier: ImageClassifier?
    private val labels: List<String>

    init {
        // Load labels first
        labels = loadLabels(context)

        // Initialize classifier with error handling
        classifier = try {
            val options = ImageClassifier.ImageClassifierOptions.builder()
                .setMaxResults(1)
                .build()

            ImageClassifier.createFromFileAndOptions(
                context,
                "model.tflite",
                options
            )
        } catch (e: Exception) {
            Log.e("TFLiteCurrencyClassifier", "Failed to initialize classifier", e)
            null
        }

        if (classifier != null) {
            Log.d("TFLiteCurrencyClassifier", "Model loaded successfully")
            Log.d("TFLiteCurrencyClassifier", "Available labels: $labels")
        } else {
            Log.e("TFLiteCurrencyClassifier", "Model failed to load")
        }
    }

    private fun loadLabels(context: Context): List<String> {
        return try {
            val inputStream = context.assets.open("labels.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val labels = mutableListOf<String>()
            reader.useLines { lines ->
                lines.forEach { label ->
                    // For labels like "0 Real 200 Notes", extract "Real 200 Notes"
                    val cleanedLabel = label.substringAfter(" ").trim()
                    labels.add(cleanedLabel)
                }
            }
            Log.d("TFLiteCurrencyClassifier", "Successfully loaded ${labels.size} labels: $labels")
            labels
        } catch (e: Exception) {
            Log.e("TFLiteCurrencyClassifier", "Failed to load labels.txt", e)
            listOf("Real 200 Notes", "Fake 200 Notes") // Fallback
        }
    }

    fun classify(bitmap: Bitmap): Pair<String, Float> {
        if (classifier == null) {
            Log.e("TFLiteCurrencyClassifier", "Classifier not initialized")
            return Pair("Error", 0f)
        }

        return try {
            // Resize bitmap to expected input size - try common sizes
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val tensorImage = TensorImage.fromBitmap(resizedBitmap)

            val results: List<Classifications> = classifier.classify(tensorImage)

            if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
                val category = results[0].categories[0]

                val labelIndex = category.label.toIntOrNull()
                val labelName = if (labelIndex != null && labelIndex < labels.size) {
                    labels[labelIndex]
                } else {
                    category.label
                }

                Log.d("TFLiteCurrencyClassifier", "Detection: '$labelName' (${(category.score * 100).toInt()}%)")

                Pair(labelName, category.score)
            } else {
                Pair("Unknown", 0f)
            }
        } catch (e: Exception) {
            Log.e("TFLiteCurrencyClassifier", "Classification error", e)
            Pair("Error", 0f)
        }
    }
}