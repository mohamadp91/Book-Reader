package com.example.bookreader.screens.login

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.R
import com.example.bookreader.data.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val goTrue: GoTrue,
    private val application: Application
) :
    AndroidViewModel(application = application) {

    var result by mutableStateOf<ResultState<*>>(ResultState.IDLE)
    private set

    fun signInEmailWithPassword(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    result = ResultState.Loading
                    val response = goTrue.loginWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    result = ResultState.Success(response)
                } catch (ex: Exception) {
                    result = ResultState.Error(ex)
                }
            }
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSignedUp: () -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    result = ResultState.Loading
                    goTrue.signUpWith(provider = Email) {
                        this.email = email
                        this.password = password
                    }
                    onSignedUp.invoke()
                    result = ResultState.IDLE
                } catch (ex: Exception) {
                    val errorMessage = when (ex) {
                        is RestException -> "Your Email is invalid"
                        else -> application.getString(R.string.no_connection_string)
                    }
                    result = ResultState.Error(Exception(errorMessage))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            goTrue.invalidateSession()
        }
    }
}