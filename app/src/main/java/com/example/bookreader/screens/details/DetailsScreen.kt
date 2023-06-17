package com.example.bookreader.screens.details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.Item
import com.example.bookreader.util.joinToStringNullable
import com.example.bookreader.widgets.BookImage

@Composable
fun DetailsScreen(
    bookId: String,
    navController: NavController,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {

    val item = produceState<ResultState<*>>(initialValue = ResultState.Loading,
        producer = {
            this.value = detailsViewModel.getBookById(bookId)
        })
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        when (item.value) {
            is ResultState.Success -> {
                val book = (item.value as ResultState.Success<*>).data as Item
                DetailsScreenUi(book, navController)
            }
            is ResultState.Loading -> {
                LinearProgressIndicator()
            }
            is ResultState.Error -> {
                var dialogState by remember {
                    mutableStateOf(false)
                }
                ErrorDialog(error = (item.value as ResultState.Error).exception.message) {
                    dialogState = !dialogState
                }
            }
            else -> {

            }
        }
    }
}

@Composable
fun DetailsScreenUi(item: Item, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BookImage(
            bookUrl = item.volumeInfo.imageLinks?.let {
                if (it !== null) it.smallThumbnail else ""
            },
            imageModifier = Modifier
                .size(130.dp)
                .background(color = Color.White, shape = CircleShape)
                .padding(16.dp)
                .clip(CircleShape)
        )
        val textModifier = Modifier.padding(top = 6.dp)
        Text(
            text = item.volumeInfo.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            modifier = textModifier,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Published: ${item.volumeInfo.publishedDate}",
            style = MaterialTheme.typography.titleMedium,
            modifier = textModifier
        )
        Text(
            text = "Authors: [" + item.volumeInfo.authors.joinToStringNullable() + " ]",
            style = MaterialTheme.typography.titleMedium,
            modifier = textModifier
        )

        Text(
            text = "Page count: ${item.volumeInfo.pageCount}",
            style = MaterialTheme.typography.titleMedium,
            modifier = textModifier
        )

        Text(
            text = "Categories: [ " + item.volumeInfo.categories.joinToStringNullable() + " ]",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Justify,
            modifier = textModifier,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(5.dp))

        val desc =
            if (item.volumeInfo.description.isNullOrEmpty()) "No description" else item.volumeInfo.description

        val cleanDescription = HtmlCompat.fromHtml(
            desc,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        LazyColumn(
            modifier = textModifier
                .border(
                    BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.secondary
                    ), shape = RoundedCornerShape(6.dp)
                )
                .padding(vertical = 6.dp)
                .height(300.dp).fillMaxWidth()
        ) {
            item {
                Text(
                    text = cleanDescription,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Justify,
                    lineHeight = 25.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        ButtonRow(navController = navController, item)
    }
}

@Composable
fun ButtonRow(
    navController: NavController,
    item: Item,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
        Button(
            onClick = {
                detailsViewModel.saveBookInDb(item) {
                    Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = ""
            )
        }
    }
}
