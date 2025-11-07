package com.teamdrishty.cashguard.analysis

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.BufferedReader
import java.io.InputStreamReader

class TFLiteCurrencyClassifier(context: Context) {

    private val classifier: ImageClassifier
    private val labels: List<String>

    init {
        // Load labels from labels.txt
        labels = loadLabels(context)

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(1)
            .build()

        classifier = ImageClassifier.createFromFileAndOptions(
            context,
            "model.tflite", // This will look in assets folder
            options
        )
    }

    private fun loadLabels(context: Context): List<String> {
        return try {
            val inputStream = context.assets.open("labels.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val labels = mutableListOf<String>()
            reader.useLines { lines ->
                lines.forEach { label ->
                    labels.add(label.trim())
                }
            }
            labels
        } catch (e: Exception) {
            // Fallback labels if file not found
            listOf("fake", "real", "unknown")
        }
    }

    fun classify(bitmap: Bitmap): Pair<String, Float> {
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val results: List<Classifications> = classifier.classify(tensorImage)

        if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
            val category = results[0].categories[0]
            val labelIndex = category.label.toIntOrNull()

            // Use the label from your labels.txt file
            val labelName = if (labelIndex != null && labelIndex < labels.size) {
                labels[labelIndex]
            } else {
                category.label
            }

            return Pair(labelName, category.score)
        }
        return Pair("Unknown", 0f)
    }
}