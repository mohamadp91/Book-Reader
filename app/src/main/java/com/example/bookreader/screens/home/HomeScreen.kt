package com.example.bookreader.screens.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.DrawerMenuContent
import com.example.bookreader.components.ReaderTopBar
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
    drawerMenuState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ReaderTopBar(
                navController = navController,
                isMainScreen = true
            ) {
                scope.launch {
                    drawerMenuState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        },
    ) {
    }
}