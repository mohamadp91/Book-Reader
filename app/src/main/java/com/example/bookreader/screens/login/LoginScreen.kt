package com.example.bookreader.screens.login

import android.util.Log
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookreader.widgets.CustomTextFiled
import com.example.bookreader.widgets.PasswordTextFiled

@Preview(showBackground = true)
@Composable
fun LoginScreen(navController: NavController? = null) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
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
                var showLoginScreenState by rememberSaveable {
                    mutableStateOf(false)
                }
                Spacer(modifier = Modifier.height(50.dp))
                UserForm(isLoginScreen = showLoginScreenState) { e, pwd ->
                    Log.i("tag", "email is $e pass is $pwd")
                }
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
                            .clickable {
                                showLoginScreenState = !showLoginScreenState
                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isLoginScreen: Boolean,
    onSubmit: (email: String, password: String) -> Unit
) {
    val emailState = rememberSaveable {
        mutableStateOf("")
    }
    val passwordState = rememberSaveable {
        mutableStateOf("")
    }
    var isFormValid = remember(emailState.value, passwordState.value) {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current


    CustomTextFiled(
        emailState,
        isFormValidState = isFormValid,
        icon = Icons.Default.Email,
        label = "Email",
        keyboardType = KeyboardType.Email
    )
    PasswordTextFiled(
        isFormValidState = isFormValid,
        passwordState = passwordState,
        label = "Password"
    )
    if (!isLoginScreen) {
        val verifyPasswordState = remember {
            mutableStateOf("")
        }
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

