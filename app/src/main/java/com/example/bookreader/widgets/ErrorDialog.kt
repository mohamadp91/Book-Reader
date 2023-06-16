package com.example.bookreader.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorDialog(
    error: String?,
    buttonTitle: String = "close",
    onDismissRequest: () -> Unit
) {
    androidx.compose.material3.AlertDialog(onDismissRequest = {},
        modifier = Modifier
            .wrapContentSize()
            .padding(6.dp),
        title = { Text(text = "Error") },
        confirmButton = {},
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = buttonTitle)
            }
        },
        text = {
            Text(
                text = error
                    ?: "An error occurred",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
        }
    )
}