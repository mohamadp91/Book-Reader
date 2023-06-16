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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookreader.R
import com.example.bookreader.models.BookModel
import com.example.bookreader.widgets.CustomIconButton
import com.example.bookreader.util.joinToStringNullable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHorizontalListCard(bookModel: BookModel, onClick: (id: String) -> Unit) {
    Card(
        onClick = {
            bookModel.id?.let { onClick.invoke(it) }
        },
        modifier = Modifier
            .size(180.dp, 215.dp)
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
                        .size(80.dp, 100.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                )
                BookStats()
            }
            BookModelInfoUi(bookModel.title, bookModel.authors)
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                ReadingStatusBadge()
            }
        }

    }
}

@Composable
fun BookStats(
    rate: Float? = null,
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
fun BookModelInfoUi(
    title: String? = null,
    authors: List<String?> = emptyList()
) {

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(
                top = 6.dp,
                start = 6.dp,
                bottom = 3.dp
            )
    ) {
        Text(
            text = title ?: "Book Title",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "[" + authors.joinToStringNullable() + "]",
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun ReadingStatusBadge(isStarted: Boolean = false) {

    val (backgroundColor, textColor) = with(MaterialTheme.colorScheme) {
        if (isStarted) primary to onPrimary else secondary to onSecondary
    }

    val text = if (isStarted) "Reading..." else "Not Started"

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
                .padding(vertical = 6.dp)
        )
    }
}