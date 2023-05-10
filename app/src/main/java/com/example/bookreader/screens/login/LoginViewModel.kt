package com.example.bookreader.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {

    private var _result: MutableStateFlow<ResultState<*>> = MutableStateFlow(ResultState.IDLE)
    val result get() = _result.asStateFlow()

    fun signInEmailWithPassword(
        email: String,
        password: String
    ) = viewModelScope.launch {
        try {
            _result.value = ResultState.Loading
            // sign in
        } catch (ex: Exception) {
            _result.value = ResultState.Error(ex)
            // exception
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSignedUp: () -> Unit
    ) = viewModelScope.launch {
        try {
            _result.value = ResultState.Loading
//           sign up

//            if email is verified
//            saveUserInDb
//                } else {
//            Log.e("TAG", "createUserWithEmail:failure  " + task.exception)
//            _result.value =
//                ResultState.Error(java.lang.Exception("An error occurred, try again later"))
        } catch (ex: Exception) {
            _result.value = ResultState.Error(ex)
        }
    }

    private fun saveUserInDb(displayName: String?) {
        val uid = 0
        val user = mapOf<String, Any>(
            "user_id" to uid,
            "display_name" to (displayName ?: "Anonymous"),
        )
//        saveInDb
    }

    private fun onSignedUp() {
//        if (currentUser.isEmailVerified) {
        //        saveUserInDb()
//            val db =
//            db.collection("users").add(user).addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    _result.value = ResultState.Success<String>(uid)
//                } else {
//                    _result.value =
//                        ResultState.Error(java.lang.Exception("An error occurred, try again later"))
//                }
//            }
//        }
    }
}