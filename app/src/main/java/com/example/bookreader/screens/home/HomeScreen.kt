package com.example.bookreader.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.BookHorizontalListCard
import com.example.bookreader.components.DrawerMenuContent
import com.example.bookreader.components.ReaderTopBar
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookModel
import com.example.bookreader.models.BookReadingStatus
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.widgets.FabButton
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val user = homeViewModel.user.collectAsState().value

    val drawerMenuState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            if (user != null)
                DrawerMenuContent(user = user, navController = navController)
        },
        drawerState = drawerMenuState
    ) {
        HomeUiContent(navController = navController, drawerMenuState)
    }

}

@Composable
fun HomeUiContent(
    navController: NavController,
    drawerMenuState: DrawerState,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ReaderTopBar(
                navController = navController,
            ) {
                scope.launch {
                    drawerMenuState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        },
        floatingActionButton = {
            FabButton {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            BookListSection(navController)
        }
    }
}

@Composable
fun TitleSection(title: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = title, style = TextStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
fun BookListSection(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val allBooksState =
        produceState<ResultState<*>>(initialValue = ResultState.Loading, producer = {
            value = homeViewModel.getBooksFromRemoteDb()
        })

    when (allBooksState.value) {
        is ResultState.Success -> {
            val booksList = (allBooksState.value as ResultState.Success).data as List<BookModel>
            if (booksList.isNullOrEmpty()) {
                Text("No reading list found")
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    val (activityBooks, readingBooks) = booksList.partition { book ->
                        book.startReading != null
                    }

                    BookListUi(
                        title = "Your Activity...",
                        books = activityBooks,
                        navController = navController
                    )
                    BookListUi(
                        title = "Reading Book",
                        books = readingBooks,
                        navController = navController
                    )
                }
            }
        }
        is ResultState.Error -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "An error occurred please check your connection",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is ResultState.Loading -> {
            LinearProgressIndicator()
        }
        else -> {}
    }
}

@Composable
fun BookListUi(
    title: String,
    books: List<BookModel>,
    navController: NavController
) {
    TitleSection(title = title)
    LazyRow(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 12.dp)
    ) {


        items(items = books) { book ->
            val readingStatus = when {
                book.endReading != null -> BookReadingStatus.Finished
                book.endReading == null && book.startReading != null -> BookReadingStatus.Reading
                else -> BookReadingStatus.NotStarted
            }
            BookHorizontalListCard(book, readingStatus) {
                navController.navigate(ReaderScreens.UpdateScreen.name + "/" + book.bookId)
            }
        }
    }
}

