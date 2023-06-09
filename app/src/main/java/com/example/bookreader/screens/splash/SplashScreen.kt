package com.example.bookreader.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.data.ResultState
import com.example.bookreader.data.UserDatabaseStates
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.util.navigateToDestinationAndRemovePrevious
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val scale = remember {
        Animatable(0f)
    }
    SplashScreenUi(scale.value)

    val userState = splashViewModel.userSaveState.value
    val destination =
        if (userState is ResultState.Success) {
            when (userState.data) {
                UserDatabaseStates.SavedUser -> ReaderScreens.HomeScreen
                UserDatabaseStates.UnAuthenticated -> ReaderScreens.LoginScreen
                else -> null
            }
        } else if (userState is ResultState.Error) {
            ErrorDialog(
                error = userState.exception.message,
                buttonTitle = "Retry"
            ) {
                splashViewModel.checkUserState()
            }
            null
        } else {
            null
        }

    LaunchedEffect(key1 = destination) {
        if (destination != null) {
            navController.navigateToDestinationAndRemovePrevious(
                destination.name,
                ReaderScreens.SplashScreen.name
            )
        }
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        splashViewModel.checkUserState()
        delay(3000)
    }
}

@Composable
fun SplashScreenUi(scale: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scale(scale),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(350.dp)
                .padding(20.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer, CircleShape
                )
                .border(BorderStroke(2.dp, Color.DarkGray), shape = CircleShape),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Book Reader", style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = "Read, Change Yourself",
                modifier = Modifier.padding(top = 30.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 20.sp,
                )
            )
        }
    }
}