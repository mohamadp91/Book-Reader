package com.example.bookreader.screens.home

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.models.UserModel

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val user = homeViewModel.user.collectAsState().value

    Scaffold() {
        if(user != null)
        Text("welcome to home ${user.firstName}")
        else {
            Text("welcome to home")
        }
    }
}