package com.example.docreader.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookreader.models.Doc
import com.example.bookreader.util.getImageUrlByOLId
import com.example.bookreader.util.joinToStringNullable
import com.example.bookreader.widgets.BookImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookVerticalListCard(doc: Doc, onClick: () -> Unit) {
    Card(
        onClick = { onClick.invoke() },
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
            val bookOLId = doc.lending_edition_s ?: doc.key.substringAfter("/works/")

            BookImage(
                bookCoverUrl = getImageUrlByOLId(bookOLId)
            )
            BookInfo(doc)
        }
    }
}

@Composable
fun BookInfo(doc: Doc) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = doc.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        VerticalCardTextView(
            title = "authors",
            text = "[ ${doc.author_name.joinToStringNullable()} ]",
        )
        VerticalCardTextView(
            title = "publish",
            text = "${doc.first_publish_year}",
        )
        VerticalCardTextView(
            title = "subjects",
            text = "[ ${doc.subject.joinToStringNullable()} ]",
        )
    }
}

@Composable
fun VerticalCardTextView(title: String, text: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(ParagraphStyle(textAlign = TextAlign.Left, lineHeight = 18.sp)) {
                withStyle(
                    SpanStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray.copy(.8f)
                    )
                ) {
                    append("$title : ")
                }
                withStyle(SpanStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)) {
                    append("\t$text")
                }
            }
        },
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2
    )
}