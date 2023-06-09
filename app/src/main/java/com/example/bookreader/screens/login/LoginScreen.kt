package com.example.bookreader.screens.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.components.ErrorDialog
import com.example.bookreader.data.ResultState
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.util.navigateToDestinationAndRemovePrevious
import com.example.bookreader.widgets.CustomTextFiled
import com.example.bookreader.widgets.PasswordTextFiled

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            ColumnContent(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun ColumnContent(navController: NavController, viewModel: LoginViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(16.dp)
    ) {
        TextContent()
        UserFormContent(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun TextContent() {
    Text(
        "Book Reader",
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier.padding(top = 18.dp)
    )
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun UserFormContent(navController: NavController, viewModel: LoginViewModel) {
    val userSignedUp = rememberSaveable { mutableStateOf(false) }
    var showLoginScreenState by rememberSaveable { mutableStateOf(false) }

    Text(
        if (showLoginScreenState) "Login" else "Sign Up",
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier.padding(top = 18.dp)
    )

    UserForm(
        isLoginScreen = showLoginScreenState,
        onSubmit = { email, password ->
            if (showLoginScreenState)
                viewModel.signInEmailWithPassword(email, password)
            else
                viewModel.createUserWithEmailAndPassword(email, password) {
                    showLoginScreenState = true
                    userSignedUp.value = true
                }
        }
    )

    ShowSignedUpMessage(showMessage = userSignedUp)

    RowContent(showLoginScreenState = showLoginScreenState) {
        showLoginScreenState = !showLoginScreenState
    }

    val loginResultState = viewModel.result

    when (loginResultState) {
        is ResultState.Success -> {
            LaunchedEffect(key1 = true, block = {
                navController.navigateToDestinationAndRemovePrevious(
                    ReaderScreens.UserScreen.name,
                    ReaderScreens.LoginScreen.name
                )
            })
        }
        is ResultState.Error -> {
            var dialogState by rememberSaveable { mutableStateOf(true) }
            if (dialogState)
                ErrorDialog(error = loginResultState.exception.message) {
                    dialogState = false
                }
        }
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }
        is ResultState.IDLE -> {
        }
    }
}

@Composable
fun RowContent(showLoginScreenState: Boolean, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            if (showLoginScreenState) "New User?" else "Already have an account?",
        )
        Text(
            text = if (showLoginScreenState) "Sign up" else "Login",
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 3.dp)
                .clickable { onClick.invoke() }
        )
    }
}

@Composable
fun ShowSignedUpMessage(showMessage : MutableState<Boolean>) {
    val context = LocalContext.current

    if (showMessage.value)
        Toast.makeText(context, "Please verify your email", Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isLoginScreen: Boolean,
    onSubmit: (email: String, password: String) -> Unit,
) {
    val emailState = rememberSaveable { mutableStateOf("") }
    val passwordState = rememberSaveable { mutableStateOf("") }
    val isFormValid = remember(emailState.value, passwordState.value) { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    CustomTextFiled(
        emailState,
        isFormValidState = isFormValid,
        icon = Icons.Default.Email,
        label = "Email",
        keyboardType = KeyboardType.Email,
        min = 6
    )
    PasswordTextFiled(
        isFormValidState = isFormValid,
        passwordState = passwordState,
        label = "Password"
    )

    if (!isLoginScreen) {
        val verifyPasswordState = remember { mutableStateOf("") }
        val passwordVerified = remember(verifyPasswordState.value, passwordState.value) {
            verifyPasswordState.value == passwordState.value
        }

        PasswordTextFiled(
            isFormValidState = isFormValid,
            passwordState = verifyPasswordState,
            label = "Verify Password"
        )

        isFormValid.value = isFormValid.value && passwordVerified
    }

    Button(
        onClick = {
            onSubmit.invoke(
                emailState.value,
                passwordState.value
            )
            keyboardController?.hide()
        },
        enabled = isFormValid.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
    ) {
        Text(
            text = if (isLoginScreen) "Login" else "Sign Up",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}


