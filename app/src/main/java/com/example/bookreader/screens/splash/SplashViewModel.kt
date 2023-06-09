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

    fun fetchUserRemote() {
        val userId = userRepository.getCurrentUserAuth()?.id
        viewModelScope.launch {
            try {
                val user = userRepository.fetchUserRemoteByUserId(userId!!)
                if (user != null) {
                    userRepository.saveUserLocally(user)
                    userSaveState.value = ResultState.Success(UserDatabaseStates.SavedUser)
                } else {
                    userSaveState.value =
                        ResultState.Success(UserDatabaseStates.NotSavedUser)
                }
            } catch (e: Exception) {
                userSaveState.value = ResultState.Error(e)
            }
        }
    }

    fun checkUserState() {
        val userAuth = userRepository.getCurrentUserAuth()
        if (userAuth != null) {
            val userId = userRepository.getCurrentUserAuth()?.id
            viewModelScope.launch {
                try {
                    // user id is non-null because when this fun is invoked, user UserInfo is not null
                    val isUserExists = userRepository.getUserByIdLocally(userId!!)
                    if (isUserExists)
                        userSaveState.value = ResultState.Success(UserDatabaseStates.SavedUser)
                    else
                        userSaveState.value =
                            ResultState.Success(UserDatabaseStates.SavedUserRemotely)
                } catch (e: Exception) {
                    e.printStackTrace()
                    userSaveState.value = ResultState.Error(e)
                }
            }
        } else {
            userSaveState.value = ResultState.Success(UserDatabaseStates.UnAuthenticated)
        }
    }
}
