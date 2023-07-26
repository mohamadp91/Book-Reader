package com.example.bookreader.util

import android.os.Build
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.bookreader.util.Constants.OPEN_LIB_API
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
    if (this == null || this.isEmpty()) {
        ""
    } else {
        val firstFive = this.take(5)
        val joined = firstFive.joinToString(separator)
        if (firstFive.size == this.size) {
            joined
        } else {
            "$joined..."
        }
    }

fun getImageUrlById(id:String) : String = "https://covers.openlibrary.org/b/id/$id-M.jpg"
fun getImageUrlByOLId(olId:String) : String = "https://covers.openlibrary.org/b/olid/$olId-M.jpg"
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