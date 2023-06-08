package com.example.bookreader.screens.user

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.data.UserDatabaseStates
import com.example.bookreader.models.UserModel
import com.example.bookreader.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var postgrestResult: MutableState<ResultState<*>> =
        mutableStateOf(ResultState.IDLE)
        private set

    fun getCurrentUserAuth(): UserInfo? =
        repository.getCurrentUserAuth()

    fun saveUser(user: UserModel) {
        postgrestResult.value = ResultState.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.saveUser(user)
                    postgrestResult.value = ResultState.Success(UserDatabaseStates.SavedUser)
                } catch (e: Exception) {
                    postgrestResult.value = ResultState.Error(e)
                }
            }
        }
    }

    fun invalidateSession() {
        repository.invalidateSession()
    }

}