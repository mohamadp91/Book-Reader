package com.example.bookreader.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookreader.components.BookVerticalListCard
import com.example.bookreader.components.ReaderTopBar
import com.example.bookreader.data.ResultState
import com.example.bookreader.models.BookApiModel
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.widgets.CustomTextFiled

@Composable
fun SearchScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        topBar = {
            ReaderTopBar(
                title = "Search",
                navController = navController,
                screen = ReaderScreens.SearchScreen
            ) {
                navController.popBackStack()
            }
        }
    ) {
        SearchContent(paddingValues = it, navController = navController)
    }
}

@Composable
fun SearchContent(
    paddingValues: PaddingValues,
    navController: NavController
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        SearchForm()
        VerticalBookList(navController = navController)
    }
}

@Composable
fun SearchForm(searchViewModel: SearchViewModel = hiltViewModel()) {
    var searchState = remember {
        mutableStateOf("")
    }

    var isSearchQueryValid = remember {
        mutableStateOf(false)
    }
    Column(horizontalAlignment = Alignment.End) {
        CustomTextFiled(
            textState = searchState,
            icon = Icons.Default.Search,
            label = "Search",
            keyboardType = KeyboardType.Text,
            isFormValidState = isSearchQueryValid
        )
        Button(
            onClick = {
                searchViewModel.getBookApiModel(searchState.value)
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            enabled = isSearchQueryValid.value
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun VerticalBookList(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val result = searchViewModel.result.value

    when (result) {
        is ResultState.Success -> {
            val books = (result.data as BookApiModel).items
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(books) {
                    BookVerticalListCard(book = it, onClick = { bookId ->
                        navController.navigate(ReaderScreens.DetailsScreen.name + bookId)
                    })
                }
            }
        }
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }
        else -> {}
    }
}
