package com.example.bookreader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookreader.R
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.BookReadingStatus
import com.example.bookreader.widgets.CustomIconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHorizontalListCard(
    bookModel: BookModel,
    readingStatus: BookReadingStatus,
    onClick: (id: String) -> Unit
) {
    Card(
        onClick = {
            bookModel.bookId?.let { onClick.invoke(it) }
        },
        modifier = Modifier
            .size(200.dp, 240.dp)
            .padding(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.open_book),
                    contentDescription = "open book",
                    modifier = Modifier
                        .size(70.dp, 100.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                )
                BookStats(rate = bookModel.rate)
            }
            BookModelInfoUi(bookModel.title, bookModel.authors)
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                ReadingStatusBadge(readingStatus)
            }
        }

    }
}

@Composable
fun BookStats(
    rate: Double? = null,
    isFavorite: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .height(100.dp)
            .padding(horizontal = 6.dp)
    ) {
        CustomIconButton(
            icon = Icons.Default.Favorite,
            onClick = { /*TODO*/ },
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Gray
        )

        Card(
            elevation = CardDefaults.elevatedCardElevation(6.dp),
            modifier = Modifier
                .size(45.dp, 80.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star"
                )
                Text(text = rate?.toString() ?: "0.0")
            }
        }
    }
}

@Composable
fun ReadingStatusBadge(readingStatus: BookReadingStatus) {

    var text = readingStatus.name
    val (backgroundColor, textColor) = with(MaterialTheme.colorScheme) {
        when (readingStatus) {
            BookReadingStatus.Reading -> primary to onPrimary
            BookReadingStatus.Finished -> onSurface to surface
            else -> {
                text = "Not Started"
                secondary to onSecondary
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(topStartPercent = 50)
            )
            .width(90.dp),
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}