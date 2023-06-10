package com.example.bookreader.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookreader.R


@Composable
fun CustomTextFiled(
    textState: MutableState<String>,
    icon: ImageVector,
    label: String,
    action: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType,
    isFormValidState: MutableState<Boolean> = mutableStateOf(true),
    min: Int = 2
) {
    val isTextFieldErrorState = remember(textState.value) {
        textState.value.trim().length < min
    }
    isFormValidState.value = isTextFieldErrorState == false

    var isFieldTouched by remember {
        mutableStateOf(false)
    }

    Column {
        OutlinedTextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
                .focusRequester(FocusRequester())
                .onFocusChanged {
                    isFieldTouched = it.isFocused
                },
            textStyle = TextStyle(
                MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            label = {
                Text(label)
            },
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            shape = RoundedCornerShape(percent = 25),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = action
            ),
            singleLine = true,
            isError = isTextFieldErrorState && isFieldTouched,
            keyboardActions = KeyboardActions(onNext = {
                if (isFormValidState.value) return@KeyboardActions
            })
        )
        if (isTextFieldErrorState && isFieldTouched)
            ErrorText(title = label)
    }
}

@Composable
fun PasswordTextFiled(
    isFormValidState: MutableState<Boolean>,
    passwordState: MutableState<String>,
    action: ImeAction = ImeAction.Next,
    label: String,
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val isPasswordHasError = remember(passwordState.value) {
        passwordState.value.trim().length < 6
    }
    isFormValidState.value = isPasswordHasError == false

    var isFieldTouched by remember {
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 3.dp)
                .onFocusChanged {
                    isFieldTouched = it.hasFocus
                },
            textStyle = TextStyle(
                MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            label = {
                Text(label)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            shape = RoundedCornerShape(percent = 25),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = action
            ),
            keyboardActions = KeyboardActions {
                if (!isFormValidState.value) return@KeyboardActions
            },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val visibilityIcon =
                        if (isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    Icon(
                        painterResource(id = visibilityIcon),
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isPasswordHasError && isFieldTouched,
        )
        if (isPasswordHasError && isFieldTouched)
            ErrorText(title = "password")
    }
}

@Composable
fun ErrorText(title: String) {
    Text(
        text = "Please enter the correct $title", style = TextStyle(
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, bottom = 2.dp, top = 2.dp)
    )
}