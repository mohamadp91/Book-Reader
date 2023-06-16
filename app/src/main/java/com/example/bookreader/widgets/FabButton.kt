package com.example.bookreader.widgets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FabButton(onFabClicked: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onFabClicked.invoke()
        },
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(4.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Book")
    }
}