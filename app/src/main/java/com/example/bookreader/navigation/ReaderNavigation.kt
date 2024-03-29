package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookreader.models.BookDetails
import com.example.bookreader.screens.details.DetailsScreen
import com.example.bookreader.screens.home.HomeScreen
import com.example.bookreader.screens.login.LoginScreen
import com.example.bookreader.screens.search.SearchScreen
import com.example.bookreader.screens.splash.SplashScreen
import com.example.bookreader.screens.update.UpdateScreen
import com.example.bookreader.screens.user.UserScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ) {
        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(ReaderScreens.UserScreen.name) {
            UserScreen(navController)
        }
        composable(ReaderScreens.HomeScreen.name) {
            HomeScreen(navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            SearchScreen(navController)
        }
        composable(ReaderScreens.DetailsScreen.name + "/{bookWorkId}" + "/{authors}" + "/{pageNumbers}") {
            val bookDetails = BookDetails(
                bookId = it.arguments?.getString("bookWorkId") ?: "",
                authors = it.arguments?.getString("authors") ?: "",
                pageCount = it.arguments?.getString("pageNumbers") ?: ""
            )
            DetailsScreen(bookDetails = bookDetails, navController)
        }
        composable(ReaderScreens.UpdateScreen.name + "/{bookId}") {
            val bookId = it.arguments?.getString("bookId") ?: ""
            UpdateScreen(navController, bookId)
        }
    }
}