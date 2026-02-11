package org.company.app.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.min

object CardVisualTransformations {
    val CardNumbers = VisualTransformation { text ->
        val groups = text.chunked(4)
        val formatted = groups.joinToString(" ")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val spacesToAdd = if (offset == 0) 0
                else (offset - 1) / 4
                return (offset + spacesToAdd).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val spacesBefore = offset / 5
                val withinGroup = offset % 5
                val result = spacesBefore * 4 + min(withinGroup, 4)
                return result.coerceAtMost(formatted.length)
            }
        }
        TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    val CardDate = VisualTransformation { text ->
        val groups = text.chunked(2)
        val formatted = groups.joinToString("/")

        val numberOffsetTranslator = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 2) offset else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset == 3 -> 2
                    else -> offset - 1
                }
            }
        }
        TransformedText(androidx.compose.ui.text.AnnotatedString(formatted), numberOffsetTranslator)
    }
}