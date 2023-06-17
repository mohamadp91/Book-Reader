package com.example.bookreader.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookreader.models.Item
import com.example.bookreader.util.joinToStringNullable
import com.example.bookreader.widgets.BookImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookVerticalListCard(book: Item, onClick: (id: String) -> Unit) {
    Card(
        onClick = { onClick.invoke(book.id) },
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BookImage(bookUrl = book.volumeInfo.imageLinks.let {
                if (it !== null) it.smallThumbnail else ""
            })
            BookInfo(book)
        }
    }
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
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "publish: " + book.volumeInfo.publishedDate,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "categories: [ " + book.volumeInfo.categories.joinToStringNullable() + " ]",
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}