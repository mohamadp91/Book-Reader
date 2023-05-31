package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookreader.screens.home.HomeScreen
import com.example.bookreader.screens.login.LoginScreen
import com.example.bookreader.screens.splash.SplashScreen

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
        composable(ReaderScreens.HomeScreen.name) {
            HomeScreen(navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController)
        }
    }
}