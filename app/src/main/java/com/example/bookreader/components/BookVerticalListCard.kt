package com.example.bookreader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookreader.R
import com.example.bookreader.models.Item
import com.example.bookreader.util.joinToStringNullable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookVerticalListCard(book: Item, onClick: (id: String) -> Unit) {
    Card(
        onClick = { onClick.invoke(book.id) },
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookImage(book = book)
            BookInfo(book)
        }
    }
}

@Composable
fun BookImage(book: Item) {
    val thumbnail = getThumbnail(book)
    val imageModifier = Modifier
        .fillMaxHeight()
        .width(90.dp)
        .background(color = MaterialTheme.colorScheme.surface)

    if (thumbnail.isEmpty())
        Image(
            painter = painterResource(id = R.drawable.open_book),
            contentDescription = "book image",
            modifier = imageModifier
        )
    else
        AsyncImage(
            model = thumbnail,
            contentDescription = "Async book image",
            modifier = imageModifier
        )
}

@Composable
fun BookInfo(book: Item) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = book.volumeInfo.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "authors: [ " + book.volumeInfo.authors.joinToStringNullable() + " ]",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "publish: " + book.volumeInfo.publishedDate,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "[ " + book.volumeInfo.categories.joinToStringNullable() + " ]",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

private fun getThumbnail(book: Item): String {
    return try {
        book.volumeInfo.imageLinks.smallThumbnail
    } catch (e: Exception) {
        ""
    }
}