package com.example.bookreader.navigation

enum class ReaderScreens {
    SplashScreen,
    HomeScreen,
    SearchScreen,
    LoginScreen,
    CreateAccountScreen,
    UpdateScreen,
    DetailsScreen,
    StatsScreen;

    companion object {
        fun fromRoute(route: String?): ReaderScreens =
            when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                HomeScreen.name -> HomeScreen
                LoginScreen.name -> LoginScreen
                SearchScreen.name -> SearchScreen
                CreateAccountScreen.name -> CreateAccountScreen
                UpdateScreen.name -> UpdateScreen
                DetailsScreen.name -> DetailsScreen
                StatsScreen.name -> StatsScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route")
            }
    }
}