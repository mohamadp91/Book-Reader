package com.example.bookreader.screens.update

import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreader.R
import com.example.bookreader.components.BookModelInfoUi
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.components.ReaderTopBar
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookModel
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.TitleSection
import com.example.bookreader.util.getCustomVisualTransformation
import com.example.bookreader.util.getLocalDate
import com.example.bookreader.widgets.BookImage
import com.example.bookreader.widgets.CustomTextFiled
import kotlinx.coroutines.launch

@Composable
fun UpdateScreen(
    navController: NavController,
    bookId: String,
    viewModel: UpdateViewModel = hiltViewModel()
) {
    val bookModelState =
        produceState<ResultState<*>>(initialValue = ResultState.Loading) {
            value = viewModel.getBookById(bookId)
        }.value

    val coroutine = rememberCoroutineScope()

    Scaffold(topBar = {
        ReaderTopBar(
            navController = navController,
            screen = ReaderScreens.UpdateScreen,
            title = "Update"
        ) {
            navController.popBackStack()
        }
    }) {
        var imageUrl by remember {
            mutableStateOf("")
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "img",
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .alpha(.08f)
                    .blur(1000.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                when (bookModelState) {
                    is ResultState.Success -> {
                        val book = bookModelState.data as BookModel
                        imageUrl = book.imageUrl ?: ""
                        UpdateScreenHeaderRow(book)
                        Divider(modifier = Modifier.fillMaxWidth())
                        UpdateForm(book, navController)
                    }
                    is ResultState.Loading -> {
                        LinearProgressIndicator()
                    }
                    is ResultState.Error -> {
                        val dialogState = rememberSaveable { mutableStateOf(true) }
                        ErrorDialog(
                            error = bookModelState.exception.message,
                            "Retry"
                        ) {
                            coroutine.launch {
                                viewModel.getBookById(bookId)
                                dialogState.value = false
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun UpdateScreenHeaderRow(book: BookModel) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BookImage(
            bookCoverUrl = book.imageUrl, imageModifier = Modifier
                .size(130.dp, 170.dp)
                .clip(
                    RoundedCornerShape(percent = 5)
                )
        )
        BookModelInfoUi(book.title, book.authors)
    }
}


@Composable
fun UpdateForm(
    book: BookModel,
    navController: NavController,
    viewModel: UpdateViewModel = hiltViewModel()
) {
    val noteState = remember {
        mutableStateOf(book.notes ?: "")
    }
    val isStartReadingState = remember {
        mutableStateOf(book.startReading != null)
    }
    val isFinishReadingState = remember {
        mutableStateOf(book.endReading != null)
    }
    val ratingState = remember {
        mutableStateOf(book.rate ?: 0.0)
    }
    val showMState = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleSection(title = "Edit Book Info")
        LazyColumn(modifier = Modifier.height(150.dp)) {
            item {
                CustomTextFiled(
                    textState = noteState,
                    icon = Icons.Default.Info,
                    label = "Enter your Thoughts",
                    keyboardType = KeyboardType.Text,
                    min = 0,
                    singleLine = false,
                    roundedCorner = 5,
                    max = 500,
                    visualTransformation = getCustomVisualTransformation(11),
                    modifier = Modifier
                        .height(150.dp),
                    action = ImeAction.None
                )
            }
        }
        TitleSection(title = "Rating")
        RatingSection(ratingState)
        TitleSection(title = "Time laps")
        SwitchButtons(
            isStartReadingState,
            isFinishReadingState
        )
        UpdateActionButton(
            onDeleteClicked = {
                viewModel.deleteBookModel(book.bookId!!) {
                    showMState.value = "Deleted Successfully!"
                    navController.popBackStack()
                }
            },
            onUpdateClicked = {
                val updatedBookModel = BookModel(
                    bookId = book.bookId,
                    notes = noteState.value,
                    rate = ratingState.value,
                    startReading = if (isStartReadingState.value) getLocalDate() else null,
                    endReading = if (isFinishReadingState.value) getLocalDate() else null,
                    userId = book.userId
                )
                viewModel.updateBookModel(updatedBookModel) {
                    showMState.value = "Updated Successfully!"
                    navController.popBackStack()
                }
            })
        if (showMState.value.isNotEmpty())
            ShowMessage(showMState)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingSection(ratingState: MutableState<Double>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            var selected by remember(ratingState.value) {
                mutableStateOf((ratingState.value - i) >= 0)
            }
            val size by animateDpAsState(
                targetValue = if (selected) 50.dp else 40.dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
            val rotate by animateFloatAsState(
                targetValue = if (selected) 360f else 0f,
                animationSpec = tween(durationMillis = 500, delayMillis = 200,easing = LinearEasing)
            )
            Icon(
                painter = painterResource(id = R.drawable.star_rate),
                contentDescription = "",
                tint = if (selected) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(size)
                    .rotate(rotate)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                ratingState.value = i.toDouble()
                            }
                            MotionEvent.ACTION_UP -> {
                            }
                        }
                        true
                    }
            )
        }
    }
}

@Composable
fun SwitchButtons(
    isStartReadingState: MutableState<Boolean>,
    isFinishReadingState: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Start Reading")
        Switch(
            checked = isStartReadingState.value,
            onCheckedChange = {
                isStartReadingState.value = it
            })

        Text(text = "Finish Reading")
        Switch(
            checked = isFinishReadingState.value,
            onCheckedChange = {
                isFinishReadingState.value = it
            },
            enabled = isStartReadingState.value
        )
    }
}

@Composable
fun UpdateActionButton(
    onUpdateClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                onDeleteClicked.invoke()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Delete")
        }
        Button(
            onClick = {
                onUpdateClicked.invoke()
            },
        ) {
            Text(text = "Update")
        }
    }
}

@Composable
fun ShowMessage(
    showMState: MutableState<String>
) {
    val context = LocalContext.current
    Toast.makeText(context, showMState.value, Toast.LENGTH_LONG).show()
    showMState.value = ""
}