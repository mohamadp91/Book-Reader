package com.example.bookreader.screens.update

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (bookModelState) {
                is ResultState.Success -> {
                    val book = bookModelState.data as BookModel

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

@Composable
fun UpdateScreenHeaderRow(book: BookModel) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
    ) {
        BookImage(bookUrl = book.imageUrl)
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
        LazyColumn(modifier = Modifier.height(300.dp)) {
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
                        .height(300.dp),
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


@Composable
fun RatingSection(ratingState: MutableState<Double>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {

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