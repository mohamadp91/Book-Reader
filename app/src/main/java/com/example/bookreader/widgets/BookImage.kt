package com.example.bookreader.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookreader.R


@Composable
fun BookImage(
    bookUrl: String?, imageModifier: Modifier = Modifier
        .fillMaxHeight()
        .width(90.dp)
        .background(color = MaterialTheme.colorScheme.surface)
) {

    if (bookUrl.isNullOrEmpty())
        Image(
            painter = painterResource(id = R.drawable.open_book),
            contentDescription = "book image",
            modifier = imageModifier
        )
    else
        AsyncImage(
            model = bookUrl,
            contentDescription = "Async book image",
            modifier = imageModifier
        )
}