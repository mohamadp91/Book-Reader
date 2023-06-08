package com.example.bookreader.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.ResultState
import com.example.bookreader.data.UserDatabaseStates
import com.example.bookreader.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private var _postgrestResult: MutableStateFlow<ResultState<*>> =
        MutableStateFlow(ResultState.IDLE)
    val postgrestResult get() = _postgrestResult.asStateFlow()

    fun getCurrentUserAuth(): UserInfo? =
        userRepository.getCurrentUserAuth()

    fun fetchUserRemoteByUserId(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val user = userRepository.fetchUserRemoteByUserId(userId)
                    if (user != null) {
                        userRepository.saveUserLocally(user)
                        _postgrestResult.value = ResultState.Success(UserDatabaseStates.SavedUser)
                    } else {
                        _postgrestResult.value =
                            ResultState.Success(UserDatabaseStates.NotSavedUser)
                    }
                } catch (e: Exception) {
                    _postgrestResult.value = ResultState.Error(e)
                }
            }
        }
    }
}