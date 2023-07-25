package com.example.bookreader.util

import android.os.Build
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.*

fun NavController.navigateToDestinationAndRemovePrevious(
    destination: String,
    previousDestination: String
) {
    this.navigate(destination) {
        popUpTo(previousDestination) { inclusive = true }
    }
}

fun Collection<String?>?.joinToStringNullable(separator: String = ","): String =
    if (!this.isNullOrEmpty()) this.joinToString(separator) else ""

fun getLocalDate(): LocalDate {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.parse(java.time.LocalDate.now().toString())
    } else {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd").format(date)
        LocalDate.parse(formatter)
    }
}

fun getCustomVisualTransformation(maxLines: Int): VisualTransformation {
    val visualTransformation = VisualTransformation {
        var newlineCount = 0
        var filteredText = ""
        it.forEach { char ->
            if (char != '\n' || newlineCount < maxLines - 1) {
                filteredText += char
                if (char == '\n') {
                    newlineCount++
                }
            }
        }
        return@VisualTransformation TransformedText(
            it,
            OffsetMapping.Identity
        )
    }
    return visualTransformation
}