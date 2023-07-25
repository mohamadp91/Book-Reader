package com.example.bookreader.screens.details

import android.widget.Toast
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.DescriptionObject
import com.example.bookreader.screens.search.SearchViewModel
import com.example.bookreader.util.getImageUrlById
import com.example.bookreader.util.joinToStringNullable
import com.example.bookreader.widgets.BookImage
import kotlinx.datetime.LocalDateTime

@Composable
fun DetailsScreen(
    bookWorkId: String,
    navController: NavController,
    detailsViewModel: DetailsViewModel = hiltViewModel()) {
    val bookState = produceState<ResultState<*>>(initialValue = ResultState.Loading,
        producer = {
            this.value = detailsViewModel.getBookByWork(bookWorkId)
        })
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        when (bookState.value) {
            is ResultState.Success -> {
                val book = (bookState.value as ResultState.Success<*>).data as BookApiModel
                DetailsScreenUi(book, navController)
            }
            is ResultState.Loading -> {
                LinearProgressIndicator()
            }
            is ResultState.Error -> {
                var dialogState by remember {
                    mutableStateOf(false)
                }
                ErrorDialog(error = (bookState.value as ResultState.Error).exception.message) {
                    dialogState = !dialogState
                }
            }
            else -> {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsScreenUi(book: BookApiModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BookImage(
            bookCoverUrl = getImageUrlById(
                book.covers?.first()?.toString() ?: book.key.substringAfter("/works/")
            ),
            imageModifier = Modifier
                .size(130.dp)
                .clip(RoundedCornerShape(percent = 10))
        )
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 6.dp)
                .height(60.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    delayMillis = 500,
                    initialDelayMillis = 1000
                )
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            InfoText(
                title = "Publish date",
                text = LocalDateTime.parse(book.created.value).date.toString()
            )
            InfoText(
                title = "Authors",
                text = "[ book.author_name.joinToStringNullable() ] ",
            )
            InfoText(
                title = "Page count",
                text = "book.number_of_pages_median",
            )
            InfoText(
                title = "Subjects",
                text = book.subjects.joinToStringNullable(),
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        val desc =
            DescriptionObject.getBookDescription(book.description)

        val cleanDescription = HtmlCompat.fromHtml(
            desc,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.secondary
                    ), shape = RoundedCornerShape(6.dp)
                )
                .padding(vertical = 6.dp)
                .height(200.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = cleanDescription,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Justify,
                    lineHeight = 25.sp,
                    modifier = Modifier.padding(16.dp),
                    maxLines = 15
                )
            }
        }
        ButtonRow(navController = navController, book)
    }
}

@Composable
fun InfoText(
    title: String,
    text: String,
    modifier: Modifier = Modifier.padding(top = 6.dp)
) {
    val textColor = Color.DarkGray
    Text(
        text = buildAnnotatedString {
            withStyle(ParagraphStyle(textAlign = TextAlign.Left, lineHeight = 24.sp)) {
                withStyle(SpanStyle(fontSize = 18.sp)) {
                    withStyle(
                        SpanStyle(
                            color = textColor.copy(.8f),
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("$title : ")
                    }
                    withStyle(
                        SpanStyle(
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("\n \t$text")
                    }
                }
            }
        },
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ButtonRow(
    navController: NavController,
    book: BookApiModel,
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
        IconButtonDetailScreen(
            color = MaterialTheme.colorScheme.error,
            icon = Icons.Default.Close,
            onClick = {
                navController.popBackStack()
            },
        )
        IconButtonDetailScreen(
            color = MaterialTheme.colorScheme.primary,
            icon = Icons.Default.Check,
            onClick = {
                detailsViewModel.saveBookInDb(book) {
                    Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            },
        )
    }
}

@Composable
fun IconButtonDetailScreen(
    onClick: () -> Unit,
    icon: ImageVector,
    color: Color
) {
    IconButton(
        modifier = Modifier.size(70.dp, 60.dp),
        onClick = {
            onClick.invoke()
        },
        colors = IconButtonDefaults.iconButtonColors(containerColor = color)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = Color.White
        )
    }
}