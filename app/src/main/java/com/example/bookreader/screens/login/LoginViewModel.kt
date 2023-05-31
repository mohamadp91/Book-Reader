package com.example.bookreader.screens.login

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.R
import com.example.bookreader.data.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val goTrue: GoTrue,
    private val application: Application
) :
    AndroidViewModel(application = application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private var _result: MutableStateFlow<ResultState<*>> = MutableStateFlow(ResultState.IDLE)
    val result get() = _result.asStateFlow()

    fun signInEmailWithPassword(
        email: String,
        password: String
    ) = viewModelScope.launch {
        try {
            _result.value = ResultState.Loading
            val response = goTrue.loginWith(Email) {
                this.email = email
                this.password = password
            }
            _result.value = ResultState.Success(response)
        } catch (ex: Exception) {
            _result.value = ResultState.Error(ex)
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSignedUp: () -> Unit
    ) = viewModelScope.launch {
        try {
            _result.value = ResultState.Loading
            goTrue.signUpWith(provider = Email) {
                this.email = email
                this.password = password
            }
            onSignedUp.invoke()
            _result.value = ResultState.IDLE
        } catch (ex: Exception) {
            when (ex) {
                is RestException -> _result.value =
                    ResultState.Error(java.lang.Exception("Your Email is invalid"))
                else -> _result.value =
                    ResultState.Error(java.lang.Exception(context.getString(R.string.no_connection_string)))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            goTrue.invalidateSession()
        }
    }
}