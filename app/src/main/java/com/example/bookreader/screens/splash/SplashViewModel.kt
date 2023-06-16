package com.example.bookreader.screens.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.data.UserDatabaseStates
import com.example.bookreader.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModel() {

    var userSaveState: MutableState<ResultState<*>> =
        mutableStateOf(ResultState.IDLE)
        private set

    private fun fetchUserRemote() {
        val userAuth = userRepository.getCurrentUserAuth()
        viewModelScope.launch {
            try {
                val user = userAuth?.let { userRepository.fetchUserRemoteByUserId(it.id) }
                when {
                    user != null -> {
                        userRepository.saveUserLocally(user)
                        userSaveState.value = ResultState.Success(UserDatabaseStates.SavedUser)
                    }
                    else -> userSaveState.value = ResultState.Success(UserDatabaseStates.UnAuthenticated)
                }
            } catch (e: Exception) {
                userSaveState.value = ResultState.Error(e)
            }
        }
    }

    fun checkUserState() {
        viewModelScope.launch {
            try {
                val isUserExists = userRepository.isUserSaved()
                if (isUserExists)
                    userSaveState.value = ResultState.Success(UserDatabaseStates.SavedUser)
                else
                    fetchUserRemote()
            } catch (e: Exception) {
                e.printStackTrace()
                userSaveState.value = ResultState.Error(e)
            }
        }
    }
}
