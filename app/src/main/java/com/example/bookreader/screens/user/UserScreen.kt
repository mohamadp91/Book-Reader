package com.example.bookreader.screens.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.data.ResultState
import com.example.bookreader.data.UserDatabaseStates
import com.example.bookreader.models.UserModel
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.util.navigateToDestinationAndRemovePrevious
import com.example.bookreader.widgets.CustomTextFiled
import com.example.bookreader.widgets.ReaderTopBar

@Composable
fun UserScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    UserScreenPage(navController = navController)

    val userState = userViewModel.postgrestResult.value

    when (userState) {
        is ResultState.Loading -> {
            Column(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        is ResultState.Success -> {
            if (userState.data == UserDatabaseStates.SavedUser) {
                LaunchedEffect(key1 = true) {
                    navController.navigateToDestinationAndRemovePrevious(
                        ReaderScreens.HomeScreen.name,
                        ReaderScreens.UserScreen.name
                    )
                }
            }
        }
        is ResultState.Error -> {
            var dialogState = rememberSaveable {
                mutableStateOf(true)
            }
            if (dialogState.value)
                ErrorDialog(error = userState.exception.message) {
                    dialogState.value = false
                }
        }
        else -> {
        }
    }

    BackHandler(enabled = true) {
        userViewModel.invalidateSession()
    }
}

@Composable
fun UserScreenPage(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    var firstNameState = remember {
        mutableStateOf("")
    }
    var lastNameState = remember {
        mutableStateOf("")
    }

    val bioState = remember {
        mutableStateOf("")
    }

    val userInfo = userViewModel.getCurrentUserAuth()

    var isFormValid = remember(
        firstNameState.value,
        lastNameState.value
    ) {
        mutableStateOf(
            firstNameState.value.trim().isNotEmpty() &&
                    lastNameState.value.trim().isNotEmpty()
        )
    }

    Scaffold(topBar = {
        ReaderTopBar(
            title = "User Info",
            onButtonClicked = {},
            navController = navController,
            screen = ReaderScreens.UserScreen
        )
    }) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            CustomTextFiled(
                textState = firstNameState,
                icon = Icons.Default.Info,
                label = "First Name",
                keyboardType = KeyboardType.Text,
                isFormValidState = isFormValid
            )
            CustomTextFiled(
                textState = lastNameState,
                icon = Icons.Default.Info,
                label = "Last Name",
                keyboardType = KeyboardType.Text,
                isFormValidState = isFormValid,
                min = 2
            )
            CustomTextFiled(
                textState = bioState,
                icon = Icons.Default.Info,
                label = "Bio",
                keyboardType = KeyboardType.Text,
                min = 0
            )
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
            ) {
                Button(
                    onClick = {
                        if (userInfo !== null) {
                            val userModel = UserModel(
                                userId = userInfo.id,
                                firstName = firstNameState.value,
                                lastName = lastNameState.value,
                                email = userInfo.email!!,
                                bio = bioState.value
                            )
                            onNextClicked(
                                userModel,
                                userViewModel
                            )
                        }
                    },
                    enabled = isFormValid.value
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

fun onNextClicked(
    userModel: UserModel,
    userViewModel: UserViewModel
) {
    userViewModel.saveUser(userModel)
}
